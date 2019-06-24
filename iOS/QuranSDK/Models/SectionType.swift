//
//  SectionType.swift
//  QuranSDK
//
//  Created by Adil on 14/06/2019.
//  Copyright © 2019 Tazkiya Tech. All rights reserved.
//

import Foundation

/**
 * An enum of the different ways that the Quran is sectioned in the Madinah and Majeedi mushafs.
 */
public enum SectionType: String {
    case surah = "surah"
    case juzInMadinahMushaf = "juz_in_madinah_mushaf"
    case hizbInMadinahMushaf = "hizb_in_madinah_mushaf"
    case hizbQuarterInMadinahMushaf = "hizb_quarter_in_madinah_mushaf"
    case juzInMajeediMushaf = "juz_in_majeedi_mushaf"
    case juzQuarterInMajeediMushaf = "juz_quarter_in_majeedi_mushaf"
}
