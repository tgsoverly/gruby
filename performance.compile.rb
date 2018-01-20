class Fibonacci
  # @return(int) @params([int])
  def self.calculate(n)
    if n == 1 || n == 2
      1
    else
      calculate(n - 1) + calculate(n - 2)
    end
  end
end

loopCount = 10
fibNumber = 40
start = Time.now
loopCount.times do
  Fibonacci.calculate(fibNumber)
end
puts Time.now - start
