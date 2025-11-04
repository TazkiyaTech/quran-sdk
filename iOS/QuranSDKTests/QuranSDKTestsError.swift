//
//  QuranSDKTestsError.swift
//  QuranSDK
//
//  Created by Adil Hussain on 18/10/2025.
//  Copyright © 2025 Tazkiya Tech. All rights reserved.
//

import Foundation

struct QuranSDKTestsError: Error {
    
    let message: String
    let underlyingError: Error?
    
    init(message: String, underlyingError: Error? = nil) {
        self.message = message
        self.underlyingError = underlyingError
    }
}
