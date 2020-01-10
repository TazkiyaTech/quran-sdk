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
        
        quranDatabase = QuranDatabase()
        
        do {
            try quranDatabase.deleteDatabaseInInternalStorage()
        } catch {
            XCTFail("Failed deleting the database file in the test setup: \(error)")
        }
    }
    
    override func tearDown() {
        try? quranDatabase.closeDatabase()
    }
    
    func test_opening_the_database_in_multiple_threads_simultaneously() throws {
        let numberOfThreads = 5
        
        let expectation = XCTestExpectation(description: "Expect the Quran database open to complete")
        expectation.expectedFulfillmentCount = numberOfThreads
        
        (1...numberOfThreads).forEach { _ in openDatabaseInBackgroundThread(expectation) }
        
        wait(for: [expectation], timeout: 5)
    }
    
    func test_querying_the_database_in_multiple_threads_simultaneously() throws {
        let numberOfThreads = 5
        
        let expectation = XCTestExpectation(description: "Expect the Quran database query to complete")
        expectation.expectedFulfillmentCount = numberOfThreads
        
        (1...numberOfThreads).forEach { _ in queryDatabaseInBackgroundThread(expectation) }
        
        wait(for: [expectation], timeout: 5)
    }
    
    private func openDatabaseInBackgroundThread(_ expectation: XCTestExpectation) {
        DispatchQueue.global().async {
            do {
                try self.quranDatabase.openDatabase()
            } catch {
                XCTFail("\(error)")
            }
            
            expectation.fulfill()
        }
    }
    
    private func queryDatabaseInBackgroundThread(_ expectation: XCTestExpectation) {
        DispatchQueue.global().async {
            do {
                let ayah = try self.quranDatabase.getAyah(surahNumber: 1, ayahNumber: 1)
                XCTAssertEqual("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", ayah)
            } catch {
                XCTFail("\(error)")
            }
            
            expectation.fulfill()
        }
    }
}
