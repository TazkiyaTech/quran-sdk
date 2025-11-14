//
//  SectionMetadata.swift
//  QuranSDK
//
//  Created by Adil on 14/06/2019.
//  Copyright © 2019 Tazkiya Tech. All rights reserved.
//

import Foundation

/**
 * Metadata which describes a section of the Quran.
 */
public struct SectionMetadata: Sendable {
    
    public let sectionType: SectionType
    public let sectionNumber: Int
    public let numAyahs: Int
    public let surahNumber: Int
    public let ayahNumber: Int
    
}
