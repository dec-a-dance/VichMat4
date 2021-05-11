import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.lang.Math.pow
import java.lang.Math.sqrt
import javax.swing.JFrame
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.min
import kotlin.math.pow

class Approximation {
    val left = -100.0
    val right = 200.0
    fun approximate(x: List<Double>, y: List<Double>, n: Int){

        var linearMNK = linearApp(x,y,n)
        var expMNK = expApp(x,y,n)
        var stepMNK = stepApp(x,y,n)
        var logMNK = logApp(x,y,n)
        var squareMNK = squareApp(x,y,n)
        var MNKs = listOf<Double>(linearMNK.MNK, expMNK.MNK, stepMNK.MNK, logMNK.MNK, squareMNK.MNK)
        var minimal = MNKs.minOrNull()
        when (minimal){
            linearMNK.MNK -> {
                println("Линейная вида y=${linearMNK.a}x+${linearMNK.b} \n" +
                        "Коэффициент пирсона равен ${linearMNK.c}")
                drawLinear(linearMNK.a, linearMNK.b)
            }
            expMNK.MNK -> {
                println("Экспоненциальная вида y=${expMNK.a}x^${expMNK.b}x")
                drawExp(expMNK.a, expMNK.b)
            }
            stepMNK.MNK -> {
                println("Степенная вида y=${stepMNK.a}x^${stepMNK.b}")
                drawStep(stepMNK.a, stepMNK.b)
            }

            logMNK.MNK -> {
                println("Логарифмическая вида y=${logMNK.a}ln(x) + ${logMNK.b}")
                drawLog(logMNK.a, logMNK.b)
            }
            squareMNK.MNK -> {
                println("Квадратичная вида y=${squareMNK.a}x^2 + ${squareMNK.b}x+${squareMNK.c}")
                drawSquare(squareMNK.a, squareMNK.b, squareMNK.c)
            }
        }
    }

    fun linearApp(x: List<Double>, y: List<Double>, n: Int): AppAnswer{
        var SX: Double = 0.0
        var SXX: Double = 0.0
        var SY: Double = 0.0
        var SXY: Double = 0.0
        for (i in 0 until n){
            SX+=x[i]
            SXX+= x[i]*x[i]
            SY+=y[i]
            SXY+=x[i]*y[i]
        }
        var delta = SXX*n - SX*SX
        var delta1 = SXY*n - SX*SY
        var delta2 = SXX*SY - SX*SXY
        var a = delta1/delta
        var b = delta2/delta
        var MNK = 0.0
        var fi = 0.0
        var x_mid=SX/n
        val y_mid=SY/n
        var SXYdiff = 0.0
        var SXXdiff = 0.0
        var SYYdiff = 0.0
        for (i in 0 until n) {
            SXYdiff+=(x[i]-x_mid)*(y[i]-y_mid)
            SXXdiff+=(x[i]-x_mid)*(x[i]-x_mid)
            SYYdiff+=(y[i]-y_mid)*(y[i]-y_mid)
            fi = (a*x[i])+b
            MNK += (fi-y[i])*(fi-y[i])
        }
        val pirson = SXYdiff/(sqrt(SXXdiff*SYYdiff))
        return AppAnswer(a,b,pirson,MNK)
    }
    fun squareApp(x: List<Double>, y: List<Double>, n: Int): AppAnswer{
        var SX: Double = 0.0
        var SXX: Double = 0.0
        var SXXX: Double = 0.0
        var SXXXX = 0.0
        var SY: Double = 0.0
        var SXY: Double = 0.0
        var SXXY = 0.0
        for (i in 0 until n){
            SX+=x[i]
            SXX+= x[i]*x[i]
            SXXX+=x[i]*x[i]*x[i]
            SXXXX+=x[i]*x[i]*x[i]*x[i]
            SY+=y[i]
            SXY+=x[i]*y[i]
            SXXY+=x[i]*x[i]*y[i]
        }
        var delta = 7*SXX*SXXXX+2*SX*SXX*SXXX-SXX*SXX*SXX-7*SXXX*SXXX-SX*SX*SXXXX
        var delta1= SY*SXX*SXXXX+SXY*SXX*SXXX+SX*SXXY*SXXX-SXX*SXX*SXXY-SY*SXXX*SXXX-SXY*SX*SXXXX
        var delta2 = 7*SXY*SXXXX+SX*SXX*SXXY+SY*SXX*SXXX-SXX*SXX*SXY-SX*SY*SXXXX-7*SXXX*SXXY
        var delta3 = 7*SXX*SXXY+SX*SY*SXXX+SX*SXX*SXY-SXX*SXX*SY-SX*SX*SXXY-7*SXXX*SXY
        var a = delta3/delta
        var b = delta2/delta
        var c = delta1/delta
        var MNK = 0.0
        var fi = 0.0
        for (i in 0 until n) {
            fi = a*x[i]*x[i]+b*x[i]+c
            MNK += (fi -y[i])*(fi -y[i])
        }
        return AppAnswer(a,b,c,MNK)
    }
    fun expApp(x: List<Double>, y: List<Double>, n: Int): AppAnswer{
        var SX: Double = 0.0
        var SXX: Double = 0.0
        var SY: Double = 0.0
        var SXY: Double = 0.0
        for (i in 0 until n){
            SX+=x[i]
            SXX+= x[i]*x[i]
            SY+=ln(y[i])
            SXY+=x[i]*ln(y[i])
        }
        var delta = SXX*n - SX*SX
        var delta1 = SXY*n - SX*SY
        var delta2 = SXX*SY - SX*SXY
        var A = delta1/delta
        var B = delta2/delta
        var MNK = 0.0
        var fi = 0.0
        var a= Math.E.pow(B)
        var b = A
        for (i in 0 until n) {
            fi = a*exp(b*x[i])

            MNK += (fi -y[i])*(fi -y[i])
        }
        if (MNK.isNaN()){
            MNK = Double.POSITIVE_INFINITY
        }
        return AppAnswer(a,b,MNK)
    }
    fun stepApp(x: List<Double>, y: List<Double>, n: Int): AppAnswer{
        var SX: Double = 0.0
        var SXX: Double = 0.0
        var SY: Double = 0.0
        var SXY: Double = 0.0
        for (i in 0 until n){
            SX+=ln(x[i])
            SXX+= ln(x[i])*ln(x[i])
            SY+=ln(y[i])
            SXY+=ln(x[i])*ln(y[i])
        }
        var delta = SXX*n - SX*SX
        var delta1 = SXY*n - SX*SY
        var delta2 = SXX*SY - SX*SXY
        var A = delta1/delta
        var B = delta2/delta
        var a= Math.E.pow(B)
        var b = A
        var MNK = 0.0
        var fi = 0.0
        for (i in 0 until n) {
            fi = a * x[i].pow(b)
            MNK += (fi -y[i])*(fi -y[i])
        }
        if (MNK.isNaN()){
            MNK = Double.POSITIVE_INFINITY
        }
        return AppAnswer(a,b,MNK)
    }
    fun logApp(x: List<Double>, y: List<Double>, n: Int): AppAnswer{
        var SX: Double = 0.0
        var SXX: Double = 0.0
        var SY: Double = 0.0
        var SXY: Double = 0.0
        for (i in 0 until n){
            SX+=ln(x[i])
            SXX+= ln(x[i])*ln(x[i])
            SY+=y[i]
            SXY+=ln(x[i])*y[i]
        }
        var delta = SXX*n - SX*SX
        var delta1 = SXY*n - SX*SY
        var delta2 = SXX*SY - SX*SXY
        var A = delta1/delta
        var B = delta2/delta
        var MNK = 0.0
        var fi = 0.0
        var a = A
        var b = B
        for (i in 0 until n) {
            fi = a*ln(x[i]) + b
            MNK += (fi -y[i])*(fi -y[i])
        }
        if (MNK.isNaN()){
            MNK = Double.POSITIVE_INFINITY
        }
        return AppAnswer(a,b,MNK)
    }
    fun drawLinear(a:Double,b:Double){
        val series = XYSeries("Your fun")
        var i: Double = left
        while (i<=right) {
            series.add(i, a*i+b)
            i+=1.0
        }
        val xyDataset: XYDataset = XYSeriesCollection(series)
        val chart = ChartFactory .createXYLineChart("Your fun", "x", "y",
            xyDataset,
            PlotOrientation.VERTICAL,
            true, true, true);
        val frame = JFrame("График ответа");
        // Помещаем график на фрейм
        frame.contentPane.add(ChartPanel(chart))
        frame.setSize(800, 600)
        frame.isVisible=true
    }
    fun drawExp(a:Double,b:Double){
        val series = XYSeries("Your fun")
        var i: Double = left
        while (i<=right) {
            series.add(i, a*exp(b*i))
            i+=1.0
        }
        val xyDataset: XYDataset = XYSeriesCollection(series)
        val chart = ChartFactory .createXYLineChart("Your fun", "x", "y",
            xyDataset,
            PlotOrientation.VERTICAL,
            true, true, true);
        val frame = JFrame("График ответа");
        // Помещаем график на фрейм
        frame.contentPane.add(ChartPanel(chart))
        frame.setSize(800, 600)
        frame.isVisible=true
    }
    fun drawStep(a:Double,b:Double){
        val series = XYSeries("Your fun")
        var i: Double = left
        while (i<=right) {
            series.add(i, a*i.pow(b))
            i+=1.0
        }
        val xyDataset: XYDataset = XYSeriesCollection(series)
        val chart = ChartFactory .createXYLineChart("Your fun", "x", "y",
            xyDataset,
            PlotOrientation.VERTICAL,
            true, true, true);
        val frame = JFrame("График ответа");
        // Помещаем график на фрейм
        frame.contentPane.add(ChartPanel(chart))
        frame.setSize(800, 600)
        frame.isVisible=true
    }
    fun drawLog(a:Double,b:Double){
        val series = XYSeries("Your fun")
        var i: Double = left
        while (i<=right) {
            series.add(i, a*ln(i)+b)
            i+=1.0
        }
        val xyDataset: XYDataset = XYSeriesCollection(series)
        val chart = ChartFactory .createXYLineChart("Your fun", "x", "y",
            xyDataset,
            PlotOrientation.VERTICAL,
            true, true, true);
        val frame = JFrame("График ответа");
        // Помещаем график на фрейм
        frame.contentPane.add(ChartPanel(chart))
        frame.setSize(800, 600)
        frame.isVisible=true
    }
    fun drawSquare(a:Double,b:Double,c:Double){
        val series = XYSeries("Your fun")
        var i: Double = left
        while (i<=right) {
            series.add(i, a*i*i+b*i+c)
            i+=1.0
        }
        val xyDataset: XYDataset = XYSeriesCollection(series)
        val chart = ChartFactory .createXYLineChart("Your fun", "x", "y",
            xyDataset,
            PlotOrientation.VERTICAL,
            true, true, true);
        val frame = JFrame("График ответа");
        // Помещаем график на фрейм
        frame.contentPane.add(ChartPanel(chart))
        frame.setSize(800, 600)
        frame.isVisible=true
    }
}
