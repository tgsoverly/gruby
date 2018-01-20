@groovy.transform.CompileStatic
class Fibonacci {
  static int calculate(int n){
    if(n == 1 || n == 2){
      1
    } else {
      calculate(n-1) + calculate(n-2)
    }
  }
}

def loopCount = 10
def fibNumber = 40

def start = System.currentTimeMillis()

loopCount.times {
  Fibonacci.calculate(fibNumber)
}
println ((System.currentTimeMillis() - start)/1000.0)
