# GAPAPFixer

Small utility to fix inigmatus' Contract Pack: Giving Aircraft a Purpose not recognizing blackheart612's 
Airplane Plus crew cabins. 

I do not hold any rights of the aforementioned, amazing KSP Addons:
 
 * [GAP by inigmatus](https://github.com/inigmatus/GAP)
 * [Airplane 
Plus](https://forum.kerbalspaceprogram.com/index.php?/topic/140262-14x-144-airplane-plus-r230-full-1875m-parts-crj-series-new-jet-engine-fixes-jul-20-2018/)

## Reason

I recently got GAP and was totally stunned by the amount of fun it adds to KSP aviation.
What I especially liked, was its stock vibe. Nevertheless, I felt kind of limited with
the stock parts, thus I installed Airplane Plus, with which I'm now building a lot of 
planes. Sadly, GAP and AP didn't go along, hence this small utility.

## Requirements

As this is written in kotlin, a JRE (Java Runitme Enviornment) is required.
It was tested with a JRE 1.8.

## Usage

This is a gradle / kotlin project, which could be built like so:

```
./gradlew build
```

The resulting JAR is located in `build/libs/` folder.

If there are any releases, then go ahead an use the pre-built releas by calling:

```
java -jar GAPAPFixer-<version>.jar [<GameDataPath>]
```

Without the optional `<GameDataPath>`, the path to your KSP's `GameData` folder,
the GAPAPFixer will look for the files, assuming KSP at it's default-steam location.
See [the wiki page](https://wiki.kerbalspaceprogram.com/wiki/Root_directory#GameData) if you don't know what 
this is.

This utility is unforgiving: It will not create any backups of the files it patches.

## Effects

The utility adds the necessary contract parameters to the contracts 'Flight 102' and 'Flight 103'.

It modifies, _patches_, the files `/Contaract Packs/GAP/Flights/Flights/Airline-Flight-X.cfg` (where `X` is 
`102` and `103`).


## Contribution

Go ham! This utility is nothing special and licensed under MIT (see LICENSE file).

Please follow the Google Java Style Guide!

## Roadmap

Ultimately, this utility will be config-file based, for know it is hardcoded only for Airplane Plus.

