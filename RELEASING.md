# Publicar uma versão PT-BR

A versão pública inicial 0.7-ptbr.2 foi compilada localmente e assinada com a chave da distribuição PT-BR. O fluxo original foi arquivado em docs/upstream-workflows e não é executado neste fork.

1. Incremente versionCode/versionName e registre as alterações.
2. Configure o SDK e uma chave de assinatura própria, fora do código público. Para atualizar os APKs desta distribuição, o mantenedor deve usar a mesma chave anterior.
3. Execute os testes, lint e assembleRelease.
4. Confira assinatura, SHA-256 e os recursos do APK com tools/verify_portuguese_apk.py.
5. Publique uma tag com o código correspondente, licença, avisos e instruções de compilação.
6. Na mesma Release, publique APK, código-fonte em ZIP e SHA256SUMS.txt. Mantenha os arquivos disponíveis enquanto distribuir o APK.

Configuração local (arquivo keystore.properties, ignorado pelo Git):

```properties
storeFile=/caminho/privado/chave.p12
storePassword=SUA_SENHA
keyAlias=SEU_ALIAS
keyPassword=SUA_SENHA
```

Também são aceitas SUFLYOR_KEYSTORE_FILE, SUFLYOR_KEYSTORE_PASSWORD, SUFLYOR_KEY_ALIAS e SUFLYOR_KEY_PASSWORD. Nunca inclua chaves privadas ou senhas em commits, releases ou arquivos de código-fonte.

```sh
./gradlew testDebugUnitTest lintDebug assembleRelease
python tools/verify_portuguese_apk.py app/build/outputs/apk/release/app-release.apk --aapt2 /caminho/aapt2
```

Os componentes e suas fontes estão em THIRD_PARTY_NOTICES.md. Alterações posteriores de terceiros precisam manter a GPL-3.0 e os avisos das demais licenças. Não reutilize o certificado ou afirmações de atestado do projeto original: esta distribuição possui assinatura própria.
