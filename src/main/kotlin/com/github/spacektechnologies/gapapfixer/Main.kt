package com.github.spacektechnologies.gapapfixer

import java.io.File
import kotlin.system.exitProcess

const val STEAM_GAMEDATA_PATH = "Steam/SteamApps/common/Kerbal Space Program/GameData"
const val STEAM_GAMEDATA_PATH_OSX = "Steam/steamapps/common/Kerbal Space Program/GameData"
const val WIN_X86_LOCATION = "C:/Program Files/$STEAM_GAMEDATA_PATH"
const val WIN_X64_LOCATION = "C:/Program Files (x86)/$STEAM_GAMEDATA_PATH"
const val OSX_LOCATION = "~/Library/Application Support/$STEAM_GAMEDATA_PATH_OSX"
const val LINUX_LOCATION_1 = "~/$STEAM_GAMEDATA_PATH"
const val LINUX_LOCATION_2 = "~/.local/share/$STEAM_GAMEDATA_PATH"
const val LINUX_LOCATION_3 = "~/.steam/steam/steamapps/common/Kerbal Space Program/GameData"

const val GAP_LOCATION = "/ContractPacks/GAP"
const val FLIGHT_FILE = "/Flights/Airline-Flight-"
const val FLIGHT_102_SUFFIX = "102.cfg"
const val FLIGHT_103_SUFFIX = "103.cfg"

const val CONTRACT_PARAMETER_MK3CABIN = "PARAMETER\n" +
        "\t\t\t{\n" +
        "\t\t\t\tname = PartValidation\n" +
        "\t\t\t\ttype = PartValidation\n" +
        "\t\t\t\t\n" +
        "\t\t\t\tpart  = mk3CrewCabin\n" +
        "\t\t\t\t\n" +
        "\t\t\t\tdisableOnStateChange = false\n" +
        "\t\t\t\thideChildren = true\n" +
        "\t\t\t\thidden = true\n" +
        "\n" +
        "\t\t\t}"

const val MK3CABIN = "mk3CrewCabin"

const val PART_PLACEHOLDER = ":part:"
const val CONTRACT_PARAMETER_TEMPLATE = "PARAMETER\n" +
        "\t\t\t{\n" +
        "\t\t\t\tname = PartValidation\n" +
        "\t\t\t\ttype = PartValidation\n" +
        "\t\t\t\t\n" +
        "\t\t\t\tpart  = $PART_PLACEHOLDER\n" +
        "\t\t\t\t\n" +
        "\t\t\t\tdisableOnStateChange = false\n" +
        "\t\t\t\thideChildren = true\n" +
        "\t\t\t\thidden = true\n" +
        "\n" +
        "\t\t\t}"

val PARTS_TO_BE_ADDED = arrayOf("s1p5CrewCabin","size2CrewCabin")

fun main(args: Array<String>){
    println("GAPAPFixer")
    printUsage()
    println("OS: ${System.getProperty("os.name")}")
    println("Arch: ${System.getProperty("os.arch")}")
    var gamedataDir:File
    if(args.isNotEmpty()){
        println("Provided KSP GameData path: ${args[0]}")
        gamedataDir = File(args[0])
    }else{
        try {
            gamedataDir = findGameDataPath()
        }catch (e:IllegalArgumentException){
            println("ERROR: ${e.message}")
            exitProcess(-1)
        }
    }
    println("\nWill look for Contract Pack: Giving Aircraft a Purpose")
    val gapDir = File("${gamedataDir.path}$GAP_LOCATION")
    if(!gapDir.exists()){
        println("ERROR: GAP not found")
        exitProcess(-2)
    }
    val flight102File = File("${gapDir.path}$FLIGHT_FILE$FLIGHT_102_SUFFIX")
    patchFile(flight102File)
    val flight103File = File("${gapDir.path}$FLIGHT_FILE$FLIGHT_103_SUFFIX")
    patchFile(flight103File)
    println("Finished!")
}

fun patchFile(file:File){
    println("Patching file ${file.path}")
    val contract = file.readText()
    val name = contract.substring(0..75)
    println("Loaded: $name")
    val patchedContract = patch(contract)
    file.writeText(patchedContract)
    println("Patched $name")
}

fun patch(contract:String):String{
    val index = contract.indexOf("}",contract.indexOf(MK3CABIN))
    println("Patching at position: $index")
    val sb = StringBuilder(contract)
    sb.insert(index, createPatch())
    return sb.toString()
}

fun printUsage() {
    println("Usage: java -jar gapapfixer.jar [gameDataDir]\n" +
            "Will search for KSP automatically,\n" +
            "but the path to the GameData directory can be specified, if desired.\n" +
            "\n" +
            "Will path GAP Flight 102 and Flight 103 Contract config files")
}

fun createPatch(): String {
    val sb = StringBuilder("\n// PATCHED BY GAPAPFixer\n")
    PARTS_TO_BE_ADDED.forEach {
        sb.append(createParameterFor(it))
        sb.append("\n")
    }
    sb.append("// END OF PATCH\n\n")
    return sb.toString()
}

fun findGameDataPath(): File {
    val os = System.getProperty("os.name")
    val arch = System.getProperty("os.arch")
    var path:String
    if(os.contains("Windows")){
        if(arch.contains("64")){
            path= WIN_X64_LOCATION
        }else{
            path= WIN_X86_LOCATION
        }
    }else if(os.contains("mac")){
        path= OSX_LOCATION
    }else if(os.contains("nix")){
        path= LINUX_LOCATION_1
    }else{
        error("Unsupported OS with name $os")
    }
    val file = File(path)
    if(file.exists()){
        return file
    }else{
        throw IllegalArgumentException("No KSP GameData folder found at $path")
    }
}

fun createParameterFor(part:String):String{
    return CONTRACT_PARAMETER_TEMPLATE.replace(PART_PLACEHOLDER, part)
}
