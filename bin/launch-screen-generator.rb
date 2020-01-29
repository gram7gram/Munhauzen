#!/usr/bin/env ruby

launch_sizes = {
  "Default~iphone" => "320x480",                          # iPhone 3GS
  "Default@2x~iphone" => "640x960",                       # iPhone 4
  "Default-568h@2x~iphone" => "640x1136",                 # iPhone 5, SE

  "Default-375w-667h@2x~iphone" => "750x1334",            # iPhone 6, 7, 8
  "Default-414w-736h@3x~iphone" => "1242x2208",           # iPhone 6+, 7+, 8+

  "Default-812h@3x~iphone" => "1125x2436",                # iPhone X, Xs
  "Default-896h@2x~iphone" => "828x1792",                 # iPhone Xr
  "Default-1242h@3x~iphone" => "1242x2688",               # iPhone Xs Max

  "Default-Portrait~ipad" => "768x1024",                  # iPad
  "Default-Portrait@2x~ipad" => "1536x2048",              # iPad Air
  "Default-Portrait-834w-1112h@2x~ipad"  => "1668x2224",  # iPad Pro 10.5"
  "Default-Portrait-1194h@2x.png" => "1688x2388",         # iPad Pro 11"
  "Default-Portrait-1024w-1366h@2x~ipad" => "2048x2732",  # iPad Pro 12"

  "Default-Landscape-414w-736h@3x~iphone" => "2208x1242", # iPhone 6+ Landscape
  "Default-Landscape-812h@3x~iphone" => "2436x1125",      # iPhone X, Xs Landscape
  "Default-Landscape-896h@2x~iphone" => "1792x828",       # iPhone Xr Landscape
  "Default-Landscape-1242h@3x~iphone" => "2688x1242",     # iPhone Xs Max Landscape

  "Default-Landscape~ipad" => "1024x768",                 # iPad Landscape
  "Default-Landscape@2x~ipad" => "2048x1536",             # iPad Air Landscape
  "Default-Landscape-834w-1112h@2x~ipad"  => "2224x1668", # iPad Pro 10.5" Landscape
  "Default-Landscape-1194h@2x.png" => "2388x1668",        # iPad Pro 11"   Landscape
  "Default-Landscape-1024w-1366h@2x~ipad" => "2732x2048"  # iPad Pro 12"   Landscape
}

launch_sizes.each do |name, size|
  `convert p0.jpg -size #{size} ../ios-en/LaunchImage.launchimage/#{name}.png`
  `cp ../ios-en/LaunchImage.launchimage/#{name}.png ../ios-ru/LaunchImage.launchimage/#{name}.png`
end