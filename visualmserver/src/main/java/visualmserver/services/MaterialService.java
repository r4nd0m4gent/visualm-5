package visualmserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import visualmserver.models.Ingredient;
import visualmserver.models.Material;
import visualmserver.models.MaterialIngredient;
import visualmserver.models.SaveStatus;
import visualmserver.repositories.IngredientRepository;
import visualmserver.repositories.MaterialIngredientRepository;
import visualmserver.repositories.MaterialsRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class MaterialService {

    @Autowired
    private MaterialsRepository materialsRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private MaterialIngredientRepository materialIngredientRepository;

    /**
     * Saves the material and (re)links its ingredients, creating ingredients that do not exist yet.
     *
     * @param existingMaterial the stored version when updating, whose ingredient links are replaced; null on insert
     */
    public Material insertMaterial(Material material, Material existingMaterial) {
        // Temporarily clear materialIngredients before saving Material to avoid
        // Hibernate attempting to persist stale composite-key join rows.
        Set<MaterialIngredient> ingredients = material.getMaterialIngredients() != null
                ? new HashSet<>(material.getMaterialIngredients()) : new HashSet<>();
        material.setMaterialIngredients(new HashSet<>());

        Material savedMaterial = this.materialsRepository.save(material);

        if (existingMaterial != null) {
            this.materialIngredientRepository.deleteMaterialIngredientBySequenceNumber(existingMaterial.getSequenceNumber());
        }

        for (MaterialIngredient materialIngredient : ingredients) {
            Ingredient ingredient = materialIngredient.getIngredient();
            if (ingredient.getId() == 0) {
                Optional<Ingredient> existing = ingredientRepository.findByNameAndType(ingredient.getName(), ingredient.getType());
                if (existing.isPresent()) {
                    ingredient.setId(existing.get().getId());
                } else {
                    int newId = ingredientRepository.findMaxId() + 1;
                    ingredientRepository.insertIngredientWithId(newId, ingredient.getName(), ingredient.getType());
                    ingredient.setId(newId);
                }
            }
            this.materialIngredientRepository.insertMaterialIngredient(savedMaterial.getSequenceNumber(),
                    ingredient.getId(), materialIngredient.getAmount());
        }
        savedMaterial.setMaterialIngredients(ingredients);

        return savedMaterial;
    }

    /**
     * Next free public label number (the "#0012" shown on labels).
     */
    public long nextPublishedSequenceNumber() {
        return this.materialsRepository.getMaterialsBySaveStatus(SaveStatus.PUBLISHED).stream()
                .filter(m -> m.getSequenceNumberPublished() != null)
                .mapToLong(Material::getSequenceNumberPublished)
                .max()
                .orElse(0) + 1;
    }
}
