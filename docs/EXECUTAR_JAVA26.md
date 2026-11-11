# Executar ReTech com Java 26 — IntelliJ IDEA / Maven

## Versões adotadas

- Java (OpenJDK): **26**
- Spring Boot: **4.1.2** (compatível com Java 26)
- Apache Maven: **3.9+** (Maven 3.6.3+ é suportado pelo Spring, mas recomendamos 3.9+)
- Oracle Database: FIAP (host, porta e service name devem ser fornecidos pelo ambiente da faculdade)

## Passos no IntelliJ IDEA

1. Descompacte o ZIP e abra **a pasta que contém o `pom.xml`**, selecionando **Open as Project** / **Load Maven Project** se aparecer a opção.
2. Configure **Project SDK = JDK 26** em *File → Project Structure → Project*.
3. Em *Settings → Build, Execution, Deployment → Build Tools → Maven → Runner*, configure **JRE = JDK 26**. Defina também JDK 26 para importação Maven, se sua versão do IntelliJ oferecer essa opção.
4. Abra o painel **Maven**, clique em **Reload All Maven Projects** e execute **Lifecycle → clean** e **Lifecycle → compile**.
5. Verifique se surgiu `target/classes/br/com/fiap/retech/ReTechApplication.class`.
6. Se aparecer `ClassNotFoundException: br.com.fiap.retech.ReTechApplication`, exclua a configuração de execução antiga em *Run → Edit Configurations* e inicie pelo objetivo Maven **spring-boot:run**. Alternativamente, crie uma configuração de Application com classe principal `br.com.fiap.retech.ReTechApplication` e módulo `retech-api`.
7. Para conectar ao Oracle, configure na execução/terminal `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`. Não coloque a senha no GitHub.
8. Depois da inicialização, consulte `http://localhost:8080/actuator/health`.

## Windows PowerShell

Na pasta com o `pom.xml`, execute:

```powershell
java -version
mvn -version
mvn clean compile
```

Ambos os primeiros comandos devem mostrar Java **26**. Se o Maven estiver usando outro JDK, configure o **JAVA_HOME** para o caminho da sua instalação Java 26 e abra um novo terminal. Exemplo:

```powershell
$env:JAVA_HOME="C:\CAMINHO\PARA\jdk-26"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
mvn -version
```

Para iniciar a API, defina as variáveis Oracle na sessão (em especial a senha, de forma privada) e execute:

```powershell
mvn spring-boot:run
```

Se o Maven não for encontrado, use o painel Maven integrado do IntelliJ. Se o `clean compile` falhar, envie **a primeira mensagem de erro** da compilação, não apenas o `ClassNotFoundException` posterior.

## Por que o ZIP anterior falhava?

A versão V2 possuía `<java.version>21</java.version>` e Spring Boot 3.5.16, cujo suporte oficial vai somente até o Java 25. A V3 ajusta o `pom.xml` e o Dockerfile para o Java 26 e usa Spring Boot 4.1.2. Uma configuração de execução antiga do IntelliJ pode continuar apontando para um classpath incompleto; por isso recomendamos reimportar o Maven e recriar a configuração.

## Limitações de validação

Arquivos, referências e estrutura do Maven foram verificados. O ambiente de elaboração possui JDK 21 e não disponibiliza Maven para compilar este projeto Java 26; por isso o build completo e a conexão Oracle FIAP **devem ser validados na sua máquina**.
