# grand_theft_app

This is a simple app that uses for my talk: "GTA: Grand Theft App" at [MuHackademy 2k24](https://muhack.org/events/muhackademy-2k24/) event.
The goal of the talk is to show how with some dynamic reverse engineering we can get the api key of an app and use it to make our own app.

## What is this app about?
This is a simple app that uses the api of openAI to generate text based on the input that the user gives. The user can input a prompt and the app will generate a text based on that prompt.

## How to run this app?
This app is a simple kotlin app that uses the openAI api.
To use this app you need to have a openAI api key.
Since this is a sample app for talk the api key is intentionally hardcoded in the code -> to insert your api key you need to edit the MainActivity.kt file in the app/src/main/java/com/example/chatgptapp/MainActivity.kt folder

look for the following line in the MainActivity.kt file:
```kotlin
    private val apiKey = "YOUR_KEY" // Replace with your actual API key
```

Replace the "YOUR_KEY" with your actual openAI api key.

After that you can run the app in your android emulator or in your android device.

# What about the talk?
### The video of the talk is available [here at minute 1:15:52](https://www.youtube.com/watch?v=0heXFNPwOd0&t=9422s)
The talk will be about how to reverse engineer the app to get the api key and how to use it to make our own app.
This App is a simple example of ssl pinning bypass and api key extraction.
If you want try without pinning you can change the network_security_config.xml file in the app/src/main/res/xml folder to allow cleartext traffic.

If you want to bypass the app ssl pinning you can use the following network_security_config.xml file in the app/src/main/res/xml folder:
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">api.openai.com</domain>
        <pin-set expiration="2025-12-31">
            <!-- Add your certificate pin here -->
            <pin digest="SHA-256"></pin>
        </pin-set>
    </domain-config>
</network-security-config>
```
For the pin digest you can use the following command to get the pin:
```bash
echo | openssl s_client -connect api.openai.com:443 2>/dev/null | openssl x509 -pubkey -noout | openssl rsa -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
```





