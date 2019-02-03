# Overviewer heads

## Use at your own risk - There is no assurance that this doesn't break the rules of any server you are playing on

TODO: Get a logo

Adds support for showing the location of players on an Overviewer map (currently hardcoded to `minezmap.com` for ease of implementation) on Firefox.

Quick start:

-   Install the mod into a forge 1.12 instance.
    -   The `mod` is built using `./gradlew build` once you have the `jdk` and `gradle`.
    -   The resulting file (in `mod/build/libs/Overviewer Icons-1.0.jar`) can be moved to your mods folder
-   Install the extension into Firefox
    -   In the `extension` folder, run `npm install && npm run compile`
    -   Then select the extension for add-on debugging in Firefox

The port is currently hardcoded to `25570`
