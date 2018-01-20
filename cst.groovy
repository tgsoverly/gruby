import org.codehaus.groovy.control.*
import org.codehaus.groovy.antlr.*
import org.codehaus.groovy.syntax.*

/*
  This is a concept of transpiling ruby source into groovy and therefor into java bytecode.
*/
class CompileStaticMapping {
  static returnTypes(previousLine){
    if(previousLine?.contains("@return")){
      return "int"
    }
    return "def"
  }

  static params(line, previousLine){
    if(previousLine?.contains("@params")){
      return "int n"
    }
    return "n"
  }
}


class SourceModifierParserPlugin extends AntlrParserPlugin {
  Reduction parseCST(SourceUnit sourceUnit, Reader reader) throws CompilationFailedException {
    def text = modifyTextSource(reader.text)
    StringReader stringReader = new StringReader(text)
    super.parseCST(sourceUnit, stringReader)
  }

  String modifyTextSource(text) {
    def lines = text.tokenize('\n')*.trim()
    def previousLine
    def modified = lines.collect { line ->
      def result = line
      switch (line) {
        case ~/^#.*$/:
            result = result.replaceAll("#", "//")
            break
        case ~/^class.*$/:
            result = result + " {"
            break
        case "end":
            result = "}"
            break
        case ~/^.*do$/:
            result = result.replaceAll(" do", " {")
            break
        case "else":
            result = "} else {"
            break
        case ~/^def self\..*$/:
            def returnType = CompileStaticMapping.returnTypes(previousLine)
            def params = CompileStaticMapping.params(line, previousLine)
            def methodName = "calculate"

            result = "static ${returnType} ${methodName}(${params}) { "
            break
        case ~/^if .*$/:
            result = result.replaceAll('if ', 'if (') + " ) {"
            break
        case ~/^[a-zA-Z].* = .*$/:
            result = "def " + result
            break
      }

      result = result.replaceAll("Time.now", "System.currentTimeMillis() / 1000.0")
      previousLine = line

      result
    }.join('\n')
    // println modified
    modified
  }
}

class SourcePreProcessor extends ParserPluginFactory {
  ParserPlugin createParserPlugin() {
    new SourceModifierParserPlugin()
  }
}

def parserPluginFactory = new SourcePreProcessor()
def conf = new CompilerConfiguration(pluginFactory: parserPluginFactory)

def binding = new Binding([
  puts: { println it },
])

def shell = new GroovyShell(binding, conf)
shell.evaluate new File(args[0]).text
