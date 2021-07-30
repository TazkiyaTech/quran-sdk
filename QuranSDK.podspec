#
# Be sure to run `pod lib lint QuranSDK.podspec' and 'pod spec lint QuranSDK.podspec'
# to ensure this is a valid spec before submitting.
#
# To learn more about a Podspec see https://guides.cocoapods.org/syntax/podspec.html
#

Pod::Spec.new do |s|

  s.author = { 'Adil Hussain' => 'TazkiyaTech@gmail.com' }
  s.documentation_url = 'https://github.com/TazkiyaTech/quran-sdk/blob/master/iOS/README.md'
  s.homepage = 'https://github.com/TazkiyaTech/quran-sdk'
  s.ios.deployment_target = '10.0'
  s.license = { :type => "Apache 2.0", :file => "LICENSE" }
  s.name = 'QuranSDK'
  s.resources = 'iOS/QuranSDK/**/*.db'
  s.social_media_url = 'https://twitter.com/TazkiyaTech'
  s.source = { :git => 'https://github.com/TazkiyaTech/quran-sdk.git', :tag => 'v1.0.4' }
  s.source_files = 'iOS/QuranSDK/**/*.swift'
  s.summary = 'An iOS framework that makes it easy for you to access verses of the Quran in your iOS projects.'
  s.swift_version = '5.0'
  s.version = '1.0.4'

end
