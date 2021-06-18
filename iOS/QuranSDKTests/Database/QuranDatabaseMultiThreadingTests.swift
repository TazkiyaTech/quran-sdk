//
//  QuranDatabaseMultiThreadingTests.swift
//  QuranSDKTests
//
//  Created by Adil Hussain on 10/01/2020.
//  Copyright © 2020 Tazkiya Tech. All rights reserved.
//

import XCTest
@testable import QuranSDK

class QuranDatabaseMultiThreadingTests: XCTestCase {
    
    private var quranDatabase: QuranDatabase!
    
    override func setUp() {
        super.setUp()
        continueAfterFailure = false
        
        do {
            try QuranDatabase().deleteDatabaseInInternalStorage()
        } catch {
            XCTFail("Failed deleting the database file in the test setup: \(error)")
        }
    }
    
    func test_opening_the_database_in_multiple_simultaneous_threads_on_the_same_QuranDatabase_instance() throws {
        let numberOfThreads = 10
        
        let expectation = XCTestExpectation(description: "Expect the Quran database open to complete")
        expectation.expectedFulfillmentCount = numberOfThreads
        
        let quranDatabase = QuranDatabase()
        
        defer { try? quranDatabase.closeDatabase() }
        
        (1...numberOfThreads).forEach { _ in queryDatabaseInBackgroundThread(expectation, quranDatabase) }
        
        wait(for: [expectation], timeout: 5)
    }
    
    func test_opening_the_database_in_multiple_simultaneous_threads_on_different_QuranDatabase_instances() throws {
        let numberOfThreads = 10
        
        let expectation = XCTestExpectation(description: "Expect the Quran database query to complete")
        expectation.expectedFulfillmentCount = numberOfThreads
        
        (1...numberOfThreads).forEach { _ in queryDatabaseInBackgroundThread(expectation) }
        
        wait(for: [expectation], timeout: 5)
    }
    
    private func queryDatabaseInBackgroundThread(_ expectation: XCTestExpectation,
                                                 _ quranDatabase: QuranDatabase) {
        DispatchQueue.global().async {
            do {
                // the database query below will implicitly make a call to open the database
                let ayah = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)
                XCTAssertEqual("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", ayah)
            } catch {
                XCTFail("\(error)")
            }
            
            expectation.fulfill()
        }
    }
    
    private func queryDatabaseInBackgroundThread(_ expectation: XCTestExpectation) {
        DispatchQueue.global().async {
            let quranDatabase = QuranDatabase()
            
            defer { try? quranDatabase.closeDatabase() }
            
            do {
                // the database query below will implicitly make a call to open the database
                let ayah = try quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)
                XCTAssertEqual("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", ayah)
            } catch {
                XCTFail("\(error)")
            }
            
            expectation.fulfill()
        }
    }
}
