echo "Ruby"
ruby performance.rb

echo "Gruby - Dynamic"
groovy cst.groovy performance.rb

echo "Gruby - Compile"
groovy cst.groovy performance.compile.rb

echo "Groovy - CompileStatic"
groovy performance.groovy
