# Compose Window Styler

This is a small library that lets you use transparency, aero, acrylic, mica, and tabbed in Compose for Desktop (Windows) easily.

![Demo Screenshot](docs/sample.png)

## Usage
After adding the package from GitHub Packages (Do not forget to add the maven repository), you'll be able to use the library by adding a single line to your Window composable content as follows:

    Window(onCloseRequest = ::exitApplication)
        ApplyEffect(WindowEffect.Mica(isDark = true))
        App(true)
    }

## Acknowledgements
* [flutter_acrylic](https://github.com/alexmercerind/flutter_acrylic): This library is heavily based on flutter_acrylic
* [Swing Acrylic](https://github.com/krlvm/SwingAcrylic) as reference for Java implementation
