//
//  QuranDatabaseError.swift
//  QuranSDK
//
//  Created by Adil Hussain on 17/10/2025.
//  Copyright © 2025 Tazkiya Tech. All rights reserved.
//

public struct QuranDatabaseError: Error {
    
    public let message: String
    public let underlyingError: Error?
    
    public init(
        message: String,
        underlyingError: Error? = nil,
    ) {
        self.message = message
        self.underlyingError = underlyingError
    }
}
