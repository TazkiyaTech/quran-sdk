//
//  QuranDatabaseMultiThreadingTests.swift
//  QuranSDKTests
//
//  Created by Adil Hussain on 10/01/2020.
//  Copyright © 2020 Tazkiya Tech. All rights reserved.
//

import Testing
@testable import QuranSDK

@Suite(.serialized)
class QuranDatabaseMultiThreadingTests {
    
    init() throws {
        do {
            try QuranDatabase.deleteDatabaseInInternalStorage()
        } catch {
            throw QuranSDKTestsError(
                message: "Failed deleting the database file in the test setup",
                underlyingError: error
            )
        }
    }
    
    @Test
    func openingDatabaseInMultipleSimultaneousThreadsOnSameQuranDatabaseInstance() async throws {
        let quranDatabase = QuranDatabase()
        
        defer { try? quranDatabase.closeDatabase() }
        
        try await withThrowingTaskGroup(of: Void.self) { group in
            for _ in 1...10 {
                group.addTask {
                    let ayah = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)
                    #expect(ayah == "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
                }
            }
            try await group.waitForAll()
        }
    }
    
    @Test
    func openingDatabaseInMultipleSimultaneousThreadsOnDifferentQuranDatabaseInstances() async throws {
        try await withThrowingTaskGroup(of: Void.self) { group in
            for _ in 1...10 {
                group.addTask {
                    let quranDatabase = QuranDatabase()
                    let ayah = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)
                    #expect(ayah == "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ")
                    try quranDatabase.closeDatabase()
                }
            }
            try await group.waitForAll()
        }
    }
}
