-- -----------------------------------------------------------------------
-- update_images.sql
--
-- Sets overview_url and close_up_url on published materials by matching
-- sequence_number_published to the AUAS archive image number.
--
-- Run this script against the live MySQL database AFTER the images have
-- been copied to the server (via Docker volume or direct deployment).
-- -----------------------------------------------------------------------

-- Material sequence_number_published = 276
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00276_1.jpg' WHERE `sequence_number_published` = 276;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00276_2.jpg' WHERE `sequence_number_published` = 276;

-- Material sequence_number_published = 279
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00279_1.jpg' WHERE `sequence_number_published` = 279;

-- Material sequence_number_published = 281
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00281_1.jpg' WHERE `sequence_number_published` = 281;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00281_2.jpg' WHERE `sequence_number_published` = 281;

-- Material sequence_number_published = 283
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00283_1.jpg' WHERE `sequence_number_published` = 283;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00283_2.jpg' WHERE `sequence_number_published` = 283;

-- Material sequence_number_published = 285
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00285_1.jpg' WHERE `sequence_number_published` = 285;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00285_2.jpg' WHERE `sequence_number_published` = 285;

-- Material sequence_number_published = 288
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00288_1.jpg' WHERE `sequence_number_published` = 288;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00288_2.jpg' WHERE `sequence_number_published` = 288;

-- Material sequence_number_published = 296
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00296_1.jpg' WHERE `sequence_number_published` = 296;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00296_3.jpg' WHERE `sequence_number_published` = 296;

-- Material sequence_number_published = 302
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00302_1.jpg' WHERE `sequence_number_published` = 302;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00302_2.jpg' WHERE `sequence_number_published` = 302;

-- Material sequence_number_published = 303
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00303_1.jpg' WHERE `sequence_number_published` = 303;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00303_2.jpg' WHERE `sequence_number_published` = 303;

-- Material sequence_number_published = 304
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00304_1.jpg' WHERE `sequence_number_published` = 304;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00304_2.jpg' WHERE `sequence_number_published` = 304;

-- Material sequence_number_published = 305
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00305_1.jpg' WHERE `sequence_number_published` = 305;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00305_2.jpg' WHERE `sequence_number_published` = 305;

-- Material sequence_number_published = 307
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00307_2.jpg' WHERE `sequence_number_published` = 307;

-- Material sequence_number_published = 308
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00308_1.jpg' WHERE `sequence_number_published` = 308;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00308_2.jpg' WHERE `sequence_number_published` = 308;

-- Material sequence_number_published = 309
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00309_1.jpg' WHERE `sequence_number_published` = 309;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00309_2.jpg' WHERE `sequence_number_published` = 309;

-- Material sequence_number_published = 313
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00313_1.jpg' WHERE `sequence_number_published` = 313;

-- Material sequence_number_published = 314
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00314_1.jpg' WHERE `sequence_number_published` = 314;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00314_2.jpg' WHERE `sequence_number_published` = 314;

-- Material sequence_number_published = 319
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00319_1.jpg' WHERE `sequence_number_published` = 319;

-- Material sequence_number_published = 326
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00326_1.jpg' WHERE `sequence_number_published` = 326;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00326_2.jpg' WHERE `sequence_number_published` = 326;

-- Material sequence_number_published = 339
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00339_1.jpg' WHERE `sequence_number_published` = 339;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00339_2.jpg' WHERE `sequence_number_published` = 339;

-- Material sequence_number_published = 340
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00340_1.jpg' WHERE `sequence_number_published` = 340;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00340_2.jpg' WHERE `sequence_number_published` = 340;

-- Material sequence_number_published = 341
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00341_1.jpg' WHERE `sequence_number_published` = 341;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00341_2.jpg' WHERE `sequence_number_published` = 341;

-- Material sequence_number_published = 343
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00343_1.jpg' WHERE `sequence_number_published` = 343;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00343_2.jpg' WHERE `sequence_number_published` = 343;

-- Material sequence_number_published = 344
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00344_1.jpg' WHERE `sequence_number_published` = 344;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00344_2.jpg' WHERE `sequence_number_published` = 344;

-- Material sequence_number_published = 347
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00347_1.jpg' WHERE `sequence_number_published` = 347;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00347_2.jpg' WHERE `sequence_number_published` = 347;

-- Material sequence_number_published = 348
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00348_1.jpg' WHERE `sequence_number_published` = 348;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00348_2.jpg' WHERE `sequence_number_published` = 348;

-- Material sequence_number_published = 349
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00349_1.jpg' WHERE `sequence_number_published` = 349;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00349_2.jpg' WHERE `sequence_number_published` = 349;

-- Material sequence_number_published = 350
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00350_1.jpg' WHERE `sequence_number_published` = 350;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00350_2.jpg' WHERE `sequence_number_published` = 350;

-- Material sequence_number_published = 354
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00354_1.jpg' WHERE `sequence_number_published` = 354;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00354_2.jpg' WHERE `sequence_number_published` = 354;

-- Material sequence_number_published = 355
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00355_1.jpg' WHERE `sequence_number_published` = 355;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00355_2.jpg' WHERE `sequence_number_published` = 355;

-- Material sequence_number_published = 357
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00357_1.jpg' WHERE `sequence_number_published` = 357;

-- Material sequence_number_published = 362
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00362_1.jpg' WHERE `sequence_number_published` = 362;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00362_2.jpg' WHERE `sequence_number_published` = 362;

-- Material sequence_number_published = 363
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00363_1.jpg' WHERE `sequence_number_published` = 363;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00363_2.jpg' WHERE `sequence_number_published` = 363;

-- Material sequence_number_published = 364
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00364_1.jpg' WHERE `sequence_number_published` = 364;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00364_2.jpg' WHERE `sequence_number_published` = 364;

-- Material sequence_number_published = 367
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00367_1.jpg' WHERE `sequence_number_published` = 367;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00367_2.jpg' WHERE `sequence_number_published` = 367;

-- Material sequence_number_published = 368
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00368_1.jpg' WHERE `sequence_number_published` = 368;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00368_2.jpg' WHERE `sequence_number_published` = 368;

-- Material sequence_number_published = 372
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00372_1.jpg' WHERE `sequence_number_published` = 372;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00372_2.jpg' WHERE `sequence_number_published` = 372;

-- Material sequence_number_published = 375
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00375_1.jpg' WHERE `sequence_number_published` = 375;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00375_2.jpg' WHERE `sequence_number_published` = 375;

-- Material sequence_number_published = 378
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00378_1.jpg' WHERE `sequence_number_published` = 378;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00378_2.jpg' WHERE `sequence_number_published` = 378;

-- Material sequence_number_published = 385
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00385_1.jpg' WHERE `sequence_number_published` = 385;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00385_2.jpg' WHERE `sequence_number_published` = 385;

-- Material sequence_number_published = 386
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00386_1.jpg' WHERE `sequence_number_published` = 386;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00386_2.jpg' WHERE `sequence_number_published` = 386;

-- Material sequence_number_published = 389
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00389_1.jpg' WHERE `sequence_number_published` = 389;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00389_2.jpg' WHERE `sequence_number_published` = 389;

-- Material sequence_number_published = 391
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00391_1.jpg' WHERE `sequence_number_published` = 391;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00391_2.jpg' WHERE `sequence_number_published` = 391;

-- Material sequence_number_published = 393
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00393_1.jpg' WHERE `sequence_number_published` = 393;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00393_2.jpg' WHERE `sequence_number_published` = 393;

-- Material sequence_number_published = 394
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00394_1.jpg' WHERE `sequence_number_published` = 394;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00394_2.jpg' WHERE `sequence_number_published` = 394;

-- Material sequence_number_published = 396
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00396_1.jpg' WHERE `sequence_number_published` = 396;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00396_2.jpg' WHERE `sequence_number_published` = 396;

-- Material sequence_number_published = 398
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00398_1.jpg' WHERE `sequence_number_published` = 398;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00398_2.jpg' WHERE `sequence_number_published` = 398;

-- Material sequence_number_published = 405
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00405_1.jpg' WHERE `sequence_number_published` = 405;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00405_2.jpg' WHERE `sequence_number_published` = 405;

-- Material sequence_number_published = 406
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00406_1.jpg' WHERE `sequence_number_published` = 406;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00406_2.jpg' WHERE `sequence_number_published` = 406;

-- Material sequence_number_published = 414
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00414_1.jpg' WHERE `sequence_number_published` = 414;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00414_2.jpg' WHERE `sequence_number_published` = 414;

-- Material sequence_number_published = 417
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00417_1.jpg' WHERE `sequence_number_published` = 417;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00417_2.jpg' WHERE `sequence_number_published` = 417;

-- Material sequence_number_published = 421
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00421_1.jpg' WHERE `sequence_number_published` = 421;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00421_2.jpg' WHERE `sequence_number_published` = 421;

-- Material sequence_number_published = 423
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00423_1.jpg' WHERE `sequence_number_published` = 423;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00423_2.jpg' WHERE `sequence_number_published` = 423;

-- Material sequence_number_published = 426
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00426_1.jpg' WHERE `sequence_number_published` = 426;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00426_2.jpg' WHERE `sequence_number_published` = 426;

-- Material sequence_number_published = 434
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00434_1.jpg' WHERE `sequence_number_published` = 434;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00434_2.jpg' WHERE `sequence_number_published` = 434;

-- Material sequence_number_published = 438
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00438_1.jpg' WHERE `sequence_number_published` = 438;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00438_2.jpg' WHERE `sequence_number_published` = 438;

-- Material sequence_number_published = 445
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00445_1.jpg' WHERE `sequence_number_published` = 445;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00445_2.jpg' WHERE `sequence_number_published` = 445;

-- Material sequence_number_published = 448
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00448_1.jpg' WHERE `sequence_number_published` = 448;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00448_2.jpg' WHERE `sequence_number_published` = 448;

-- Material sequence_number_published = 460
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00460_1.jpg' WHERE `sequence_number_published` = 460;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00460_2.jpg' WHERE `sequence_number_published` = 460;

-- Material sequence_number_published = 461
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00461_1.jpg' WHERE `sequence_number_published` = 461;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00461_2.jpg' WHERE `sequence_number_published` = 461;

-- Material sequence_number_published = 463
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00463_1.jpg' WHERE `sequence_number_published` = 463;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00463_2.jpg' WHERE `sequence_number_published` = 463;

-- Material sequence_number_published = 464
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00464_1.jpg' WHERE `sequence_number_published` = 464;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00464_2.jpg' WHERE `sequence_number_published` = 464;

-- Material sequence_number_published = 467
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00467_1.jpg' WHERE `sequence_number_published` = 467;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00467_2.jpg' WHERE `sequence_number_published` = 467;

-- Material sequence_number_published = 469
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00469_1.jpg' WHERE `sequence_number_published` = 469;

-- Material sequence_number_published = 479
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00479_1.jpg' WHERE `sequence_number_published` = 479;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00479_2.jpg' WHERE `sequence_number_published` = 479;

-- Material sequence_number_published = 485
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00485_1.jpg' WHERE `sequence_number_published` = 485;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00485_2.jpg' WHERE `sequence_number_published` = 485;

-- Material sequence_number_published = 486
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00486_1.jpg' WHERE `sequence_number_published` = 486;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00486_2.jpg' WHERE `sequence_number_published` = 486;

-- Material sequence_number_published = 490
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00490_1.jpg' WHERE `sequence_number_published` = 490;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00490_2.jpg' WHERE `sequence_number_published` = 490;

-- Material sequence_number_published = 499
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00499_1.jpg' WHERE `sequence_number_published` = 499;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00499_2.jpg' WHERE `sequence_number_published` = 499;

-- Material sequence_number_published = 501
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00501_1.jpg' WHERE `sequence_number_published` = 501;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00501_2.jpg' WHERE `sequence_number_published` = 501;

-- Material sequence_number_published = 508
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00508_1.jpg' WHERE `sequence_number_published` = 508;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00508_2.jpg' WHERE `sequence_number_published` = 508;

-- Material sequence_number_published = 509
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00509_1.jpg' WHERE `sequence_number_published` = 509;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00509_2.jpg' WHERE `sequence_number_published` = 509;

-- Material sequence_number_published = 510
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00510_1.jpg' WHERE `sequence_number_published` = 510;

-- Material sequence_number_published = 511
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00511_1.jpg' WHERE `sequence_number_published` = 511;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00511_2.jpg' WHERE `sequence_number_published` = 511;

-- Material sequence_number_published = 512
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00512_1.jpg' WHERE `sequence_number_published` = 512;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00512_2.jpg' WHERE `sequence_number_published` = 512;

-- Material sequence_number_published = 514
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00514_1.jpg' WHERE `sequence_number_published` = 514;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00514_2.jpg' WHERE `sequence_number_published` = 514;

-- Material sequence_number_published = 517
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00517_1.jpg' WHERE `sequence_number_published` = 517;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00517_2.jpg' WHERE `sequence_number_published` = 517;

-- Material sequence_number_published = 527
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00527_1.jpg' WHERE `sequence_number_published` = 527;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00527_2.jpg' WHERE `sequence_number_published` = 527;

-- Material sequence_number_published = 536
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00536_1.jpg' WHERE `sequence_number_published` = 536;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00536_2.jpg' WHERE `sequence_number_published` = 536;

-- Material sequence_number_published = 541
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00541_1.jpg' WHERE `sequence_number_published` = 541;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00541_2.jpg' WHERE `sequence_number_published` = 541;

-- Material sequence_number_published = 542
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00542_1.jpg' WHERE `sequence_number_published` = 542;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00542_2.jpg' WHERE `sequence_number_published` = 542;

-- Material sequence_number_published = 543
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00543_1.jpg' WHERE `sequence_number_published` = 543;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00543_2.jpg' WHERE `sequence_number_published` = 543;

-- Material sequence_number_published = 544
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00544_3.jpg' WHERE `sequence_number_published` = 544;

-- Material sequence_number_published = 548
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00548_1.jpg' WHERE `sequence_number_published` = 548;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00548_2.jpg' WHERE `sequence_number_published` = 548;

-- Material sequence_number_published = 549
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00549_1.jpg' WHERE `sequence_number_published` = 549;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00549_2.jpg' WHERE `sequence_number_published` = 549;

-- Material sequence_number_published = 552
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00552_1.jpg' WHERE `sequence_number_published` = 552;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00552_2.jpg' WHERE `sequence_number_published` = 552;

-- Material sequence_number_published = 553
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00553_1.jpg' WHERE `sequence_number_published` = 553;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_553_2.jpg' WHERE `sequence_number_published` = 553;

-- Material sequence_number_published = 554
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00554_1.jpg' WHERE `sequence_number_published` = 554;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00554_2.jpg' WHERE `sequence_number_published` = 554;

-- Material sequence_number_published = 555
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00555_1.jpg' WHERE `sequence_number_published` = 555;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00555_2.jpg' WHERE `sequence_number_published` = 555;

-- Material sequence_number_published = 562
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00562_1.jpg' WHERE `sequence_number_published` = 562;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00562_2.jpg' WHERE `sequence_number_published` = 562;

-- Material sequence_number_published = 564
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00564_1.jpg' WHERE `sequence_number_published` = 564;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00564_2.jpg' WHERE `sequence_number_published` = 564;

-- Material sequence_number_published = 575
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00575_1.jpg' WHERE `sequence_number_published` = 575;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00575_2.jpg' WHERE `sequence_number_published` = 575;

-- Material sequence_number_published = 576
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00576_1.jpg' WHERE `sequence_number_published` = 576;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00576_3.jpg' WHERE `sequence_number_published` = 576;

-- Material sequence_number_published = 577
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00577_1.jpg' WHERE `sequence_number_published` = 577;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00577_2.jpg' WHERE `sequence_number_published` = 577;

-- Material sequence_number_published = 578
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00578_1.jpg' WHERE `sequence_number_published` = 578;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00578_2.jpg' WHERE `sequence_number_published` = 578;

-- Material sequence_number_published = 579
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00579_2.jpg' WHERE `sequence_number_published` = 579;

-- Material sequence_number_published = 590
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00590_1.jpg' WHERE `sequence_number_published` = 590;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00590_2.jpg' WHERE `sequence_number_published` = 590;

-- Material sequence_number_published = 591
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00591_1.jpg' WHERE `sequence_number_published` = 591;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00591_2.jpg' WHERE `sequence_number_published` = 591;

-- Material sequence_number_published = 592
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00592_1.jpg' WHERE `sequence_number_published` = 592;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00592_2.jpg' WHERE `sequence_number_published` = 592;

-- Material sequence_number_published = 597
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00597_1.jpg' WHERE `sequence_number_published` = 597;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00597_2.jpg' WHERE `sequence_number_published` = 597;

-- Material sequence_number_published = 598
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00598_1.jpg' WHERE `sequence_number_published` = 598;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00598_2.jpg' WHERE `sequence_number_published` = 598;

-- Material sequence_number_published = 605
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00605_1.jpg' WHERE `sequence_number_published` = 605;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00605_2.jpg' WHERE `sequence_number_published` = 605;

-- Material sequence_number_published = 606
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00606_1.jpg' WHERE `sequence_number_published` = 606;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00606_2.jpg' WHERE `sequence_number_published` = 606;

-- Material sequence_number_published = 607
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00607_1.jpg' WHERE `sequence_number_published` = 607;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00607_2.jpg' WHERE `sequence_number_published` = 607;

-- Material sequence_number_published = 611
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00611_1.jpg' WHERE `sequence_number_published` = 611;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00611_2.jpg' WHERE `sequence_number_published` = 611;

-- Material sequence_number_published = 615
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00615_1.jpg' WHERE `sequence_number_published` = 615;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00615_2.jpg' WHERE `sequence_number_published` = 615;

-- Material sequence_number_published = 618
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00618_1.jpg' WHERE `sequence_number_published` = 618;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00618_2.jpg' WHERE `sequence_number_published` = 618;

-- Material sequence_number_published = 621
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00621_1.jpg' WHERE `sequence_number_published` = 621;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00621_2.jpg' WHERE `sequence_number_published` = 621;

-- Material sequence_number_published = 622
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00622_1.jpg' WHERE `sequence_number_published` = 622;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00622_2.jpg' WHERE `sequence_number_published` = 622;

-- Material sequence_number_published = 624
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00624_1.jpg' WHERE `sequence_number_published` = 624;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00624_2.jpg' WHERE `sequence_number_published` = 624;

-- Material sequence_number_published = 628
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00628_1.jpg' WHERE `sequence_number_published` = 628;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00628_2.jpg' WHERE `sequence_number_published` = 628;

-- Material sequence_number_published = 634
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00634_1.jpg' WHERE `sequence_number_published` = 634;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00634_2.jpg' WHERE `sequence_number_published` = 634;

-- Material sequence_number_published = 648
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00648_1.jpg' WHERE `sequence_number_published` = 648;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00648_2.jpg' WHERE `sequence_number_published` = 648;

-- Material sequence_number_published = 649
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00649_2.jpg' WHERE `sequence_number_published` = 649;

-- Material sequence_number_published = 650
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00650_1.jpg' WHERE `sequence_number_published` = 650;

-- Material sequence_number_published = 651
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00651_1.jpg' WHERE `sequence_number_published` = 651;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00651_2.jpg' WHERE `sequence_number_published` = 651;

-- Material sequence_number_published = 653
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00653_1.jpg' WHERE `sequence_number_published` = 653;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00653_2.jpg' WHERE `sequence_number_published` = 653;

-- Material sequence_number_published = 654
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00654_1.jpg' WHERE `sequence_number_published` = 654;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00654_2.jpg' WHERE `sequence_number_published` = 654;

-- Material sequence_number_published = 655
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00655_1.jpg' WHERE `sequence_number_published` = 655;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00655_2.jpg' WHERE `sequence_number_published` = 655;

-- Material sequence_number_published = 657
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00657_1.jpg' WHERE `sequence_number_published` = 657;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00657_2.jpg' WHERE `sequence_number_published` = 657;

-- Material sequence_number_published = 658
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00658_1.jpg' WHERE `sequence_number_published` = 658;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00658_2.jpg' WHERE `sequence_number_published` = 658;

-- Material sequence_number_published = 659
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00659_1.jpg' WHERE `sequence_number_published` = 659;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00659_2.jpg' WHERE `sequence_number_published` = 659;

-- Material sequence_number_published = 661
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00661_1.jpg' WHERE `sequence_number_published` = 661;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00661_2.jpg' WHERE `sequence_number_published` = 661;

-- Material sequence_number_published = 662
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00662_1.jpg' WHERE `sequence_number_published` = 662;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00662_2.jpg' WHERE `sequence_number_published` = 662;

-- Material sequence_number_published = 663
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00663_1.jpg' WHERE `sequence_number_published` = 663;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00663_2.jpg' WHERE `sequence_number_published` = 663;

-- Material sequence_number_published = 664
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00664_1.jpg' WHERE `sequence_number_published` = 664;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00664_2.jpg' WHERE `sequence_number_published` = 664;

-- Material sequence_number_published = 665
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00665_1.jpg' WHERE `sequence_number_published` = 665;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00665_2.jpg' WHERE `sequence_number_published` = 665;

-- Material sequence_number_published = 666
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00666_1.jpg' WHERE `sequence_number_published` = 666;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00666_2.jpg' WHERE `sequence_number_published` = 666;

-- Material sequence_number_published = 667
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00667_1.jpg' WHERE `sequence_number_published` = 667;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00667_2.jpg' WHERE `sequence_number_published` = 667;

-- Material sequence_number_published = 668
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00668_1.jpg' WHERE `sequence_number_published` = 668;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00668_2.jpg' WHERE `sequence_number_published` = 668;

-- Material sequence_number_published = 669
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00669_1.jpg' WHERE `sequence_number_published` = 669;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00669_2.jpg' WHERE `sequence_number_published` = 669;

-- Material sequence_number_published = 670
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00670_1.jpg' WHERE `sequence_number_published` = 670;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00670_2.jpg' WHERE `sequence_number_published` = 670;

-- Material sequence_number_published = 673
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00673_1.jpg' WHERE `sequence_number_published` = 673;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00673_2.jpg' WHERE `sequence_number_published` = 673;

-- Material sequence_number_published = 675
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00675_1.jpg' WHERE `sequence_number_published` = 675;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00675_2.jpg' WHERE `sequence_number_published` = 675;

-- Material sequence_number_published = 676
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00676_1.jpg' WHERE `sequence_number_published` = 676;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00676_2.jpg' WHERE `sequence_number_published` = 676;

-- Material sequence_number_published = 677
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00677_1.jpg' WHERE `sequence_number_published` = 677;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00677_2.jpg' WHERE `sequence_number_published` = 677;

-- Material sequence_number_published = 678
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00678_1.jpg' WHERE `sequence_number_published` = 678;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00678_2.jpg' WHERE `sequence_number_published` = 678;

-- Material sequence_number_published = 679
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00679_1.jpg' WHERE `sequence_number_published` = 679;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00679_2.jpg' WHERE `sequence_number_published` = 679;

-- Material sequence_number_published = 680
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00680_1.jpg' WHERE `sequence_number_published` = 680;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00680_2.jpg' WHERE `sequence_number_published` = 680;

-- Material sequence_number_published = 681
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00681_1.jpg' WHERE `sequence_number_published` = 681;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00681_2.jpg' WHERE `sequence_number_published` = 681;

-- Material sequence_number_published = 682
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00682_1.jpg' WHERE `sequence_number_published` = 682;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00682_2.jpg' WHERE `sequence_number_published` = 682;

-- Material sequence_number_published = 683
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00683_1.jpg' WHERE `sequence_number_published` = 683;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00683_2.jpg' WHERE `sequence_number_published` = 683;

-- Material sequence_number_published = 684
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00684_1.jpg' WHERE `sequence_number_published` = 684;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00684_2.jpg' WHERE `sequence_number_published` = 684;

-- Material sequence_number_published = 685
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00685_1.jpg' WHERE `sequence_number_published` = 685;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00685_2.jpg' WHERE `sequence_number_published` = 685;

-- Material sequence_number_published = 686
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00686_1.jpg' WHERE `sequence_number_published` = 686;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00686_2.jpg' WHERE `sequence_number_published` = 686;

-- Material sequence_number_published = 687
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00687_1.jpg' WHERE `sequence_number_published` = 687;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00687_2.jpg' WHERE `sequence_number_published` = 687;

-- Material sequence_number_published = 688
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00688_1.jpg' WHERE `sequence_number_published` = 688;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00688_2.jpg' WHERE `sequence_number_published` = 688;

-- Material sequence_number_published = 690
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00690_1.jpg' WHERE `sequence_number_published` = 690;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00690_2.jpg' WHERE `sequence_number_published` = 690;

-- Material sequence_number_published = 692
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00692_1.jpg' WHERE `sequence_number_published` = 692;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00692_2.jpg' WHERE `sequence_number_published` = 692;

-- Material sequence_number_published = 693
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00693_1.jpg' WHERE `sequence_number_published` = 693;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00693_2.jpg' WHERE `sequence_number_published` = 693;

-- Material sequence_number_published = 694
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00694_1.jpg' WHERE `sequence_number_published` = 694;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00694_2.jpg' WHERE `sequence_number_published` = 694;

-- Material sequence_number_published = 695
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00695_1.jpg' WHERE `sequence_number_published` = 695;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00695_2.jpg' WHERE `sequence_number_published` = 695;

-- Material sequence_number_published = 696
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00696_1.jpg' WHERE `sequence_number_published` = 696;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00696_2.jpg' WHERE `sequence_number_published` = 696;

-- Material sequence_number_published = 697
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00697_1.jpg' WHERE `sequence_number_published` = 697;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00697_2.jpg' WHERE `sequence_number_published` = 697;

-- Material sequence_number_published = 698
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00698_1.jpg' WHERE `sequence_number_published` = 698;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00698_2.jpg' WHERE `sequence_number_published` = 698;

-- Material sequence_number_published = 699
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00699_1.jpg' WHERE `sequence_number_published` = 699;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00699_2.jpg' WHERE `sequence_number_published` = 699;

-- Material sequence_number_published = 701
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00701_1.jpg' WHERE `sequence_number_published` = 701;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00701_2.jpg' WHERE `sequence_number_published` = 701;

-- Material sequence_number_published = 702
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00702_1.jpg' WHERE `sequence_number_published` = 702;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00702_2.jpg' WHERE `sequence_number_published` = 702;

-- Material sequence_number_published = 709
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00709_1.jpg' WHERE `sequence_number_published` = 709;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00709_2.jpg' WHERE `sequence_number_published` = 709;

-- Material sequence_number_published = 710
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00710_1.jpg' WHERE `sequence_number_published` = 710;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00710_2.jpg' WHERE `sequence_number_published` = 710;

-- Material sequence_number_published = 711
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00711_1.jpg' WHERE `sequence_number_published` = 711;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00711_2.jpg' WHERE `sequence_number_published` = 711;

-- Material sequence_number_published = 713
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00713_1.jpg' WHERE `sequence_number_published` = 713;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00713_2.jpg' WHERE `sequence_number_published` = 713;

-- Material sequence_number_published = 716
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00716_1.jpg' WHERE `sequence_number_published` = 716;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00716_2.jpg' WHERE `sequence_number_published` = 716;

-- Material sequence_number_published = 717
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00717_1.jpg' WHERE `sequence_number_published` = 717;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00717_2.jpg' WHERE `sequence_number_published` = 717;

-- Material sequence_number_published = 719
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00719_1.jpg' WHERE `sequence_number_published` = 719;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00719_2.jpg' WHERE `sequence_number_published` = 719;

-- Material sequence_number_published = 722
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00722_1.jpg' WHERE `sequence_number_published` = 722;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00722_2.jpg' WHERE `sequence_number_published` = 722;

-- Material sequence_number_published = 741
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00741_1.jpg' WHERE `sequence_number_published` = 741;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00741_2.jpg' WHERE `sequence_number_published` = 741;

-- Material sequence_number_published = 742
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00742_1.jpg' WHERE `sequence_number_published` = 742;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00742_2.jpg' WHERE `sequence_number_published` = 742;

-- Material sequence_number_published = 745
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00745_1.jpg' WHERE `sequence_number_published` = 745;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00745_2.jpg' WHERE `sequence_number_published` = 745;

-- Material sequence_number_published = 746
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_00746_1.jpg' WHERE `sequence_number_published` = 746;
UPDATE `material` SET `close_up_url` = '/images/material/archive/AUAS_00746_2.jpg' WHERE `sequence_number_published` = 746;

-- Material sequence_number_published = 2079
UPDATE `material` SET `overview_url` = '/images/material/archive/AUAS_002079_1.jpg' WHERE `sequence_number_published` = 2079;

