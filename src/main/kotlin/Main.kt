import java.io.File
import java.util.*
import Approximation
import java.lang.System.exit

fun main(args: Array<String>) {
    var app = Approximation()
    println("Если вы хотите вводить с клавиатуры, введите 1, если вы хотите ввести из файла, введите 2")
    var inputVariant = readLine()
    var scan: Scanner = Scanner(System.`in`)
    var invalid = false
    when (inputVariant) {
        "1" -> {
            scan = Scanner(System.`in`)
        }
        "2" -> {
            println("Введите имя файла:")
            val fileName: String? = readLine()
            if (fileName != null){
                scan = Scanner(File(fileName))
            }
        }
    }
    println("Введите через пробел значения X и Y")
    var x: MutableList<Double> = mutableListOf()
    var y: MutableList<Double> = mutableListOf()
    while(true){
        var line = scan.nextLine()
        if(line == ""){
   //         if(x.size>=12){
                println("Ввод закончен")
                break
   //         }
   //         else{
 //               println("Недостаточно элементов")
                if(inputVariant=="2"){
                    exit(0)
                }
   //         }

        }
        var arr = line.split(" ")
        if (arr.size!=2){
            println("Инвалидная строка")
        }
        else{
            if ((arr[0].toDouble()<=0)||(arr[1].toDouble()<=0)){
                println("Возможно, что некоторые из приближений не смогут быть вычислены")
            }
            try {
                x.add(arr[0].toDouble())
                y.add(arr[1].toDouble())
            }
            catch(e: NumberFormatException){
                println("ИНвалидные значения")
            }
        }
    }
    var n = x.size
    println(n)
    app.approximate(x,y,n)
    }
