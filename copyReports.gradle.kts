//not use tasks.named("build") {
//    finalizedBy(tasks.named("copyAabToBuildFolder"))
//}
// Создаем задачу для удаления исходных файлов после перемещения
tasks.register<Delete>("deleteAabFromSource") {
    println("==========================")
    delete(fileTree("$buildDir/outputs/bundle/release").matching {
        include("*.aab")
    })
}


tasks.named("copyAabToBuildFolder") {
    mustRunAfter(tasks.named("signReleaseBundle"))
}
tasks.named("generateVersionInfo") {
    mustRunAfter(tasks.named("signReleaseBundle"))
}

tasks.named("copyAabToBuildFolder").configure {
    //dependsOn (tasks.named("build"))
    dependsOn(tasks.named("signReleaseBundle"))
    finalizedBy(tasks.named("deleteAabFromSource"))
}


//abstract class CreateFileTask : DefaultTask() {
//    @TaskAction
//    fun action() {
//        println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")
//        val file = File("myfile.txt")
//        file.createNewFile()
//        file.writeText("HELLO FROM MY TASK")
//    }
//}

//tasks.register<CreateFileTask>("copyAabToBuildFolder")


//tasks.whenTaskAdded {
//    doLast {
//        val isApk = this.name.startsWith("assemble", true)
//        val isAab = this.name.startsWith("bundle", true)
//        if (isAab || isApk) {
//            tasks.register<Copy>("copyReportsForArchiving") {
//                val outputFile = if(isAab){
//                    //"${rootDir}/generatedAAB"
//                    "D:\\build/generatedAAB"
//                } else {
//                    "${rootDir}/generatedApk"
//                }
//                val outputDirectory = file(outputFile)
//                if (!outputDirectory.exists()) {
//                    outputDirectory.mkdirs()
//                }
//                copy {
//                    from("$projectDir/release")
//                    if(isAab){
//                        include("*.aab")
//                    } else {
//                        include("*.apk")
//                    }
//                    into(outputFile)
//                }
//            }
//        }
//    }
//}
