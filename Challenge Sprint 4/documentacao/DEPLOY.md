# Guia de Deploy (Render)

Passo a passo básico para publicar a API na nuvem utilizando o Render (caso queira gerar um link online).

## 1. Subir no GitHub
1. Crie um repositório no GitHub.
2. Na pasta do projeto, envie o código:
   ```bash
   git remote add origin https://github.com/SEU-USUARIO/nome-do-repo.git
   git push -u origin main
   ```

## 2. Configurar no Render
1. Acesse [render.com](https://render.com) e crie um Web Service conectado ao seu repositório.
2. Defina as configurações:
   - **Runtime:** Java
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/devwarrior-0.0.1-SNAPSHOT.jar`
3. Nas variáveis de ambiente (`Environment Variables`), informe o acesso ao banco Oracle da FIAP:
   - `DB_USER` = seu RM (ex: `RM573332`)
   - `DB_PASSWORD` = sua senha do Oracle
   - `DB_HOST` = `oracle.fiap.com.br`

Pronto! O Render vai compilar e gerar a URL pública da API.
