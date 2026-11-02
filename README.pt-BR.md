# Suflyor PT-BR — versão modificada 0.7-ptbr.2

Base: olerast67/voice-teleprompter-android, versão 0.7, commit 20a2f73f881e15ba373d714e53795e993971e3d0.
Modificações de 1 de novembro de 2026. Esta é uma versão independente, não uma versão oficial do autor original.

## O que mudou

Atualização ptbr.2: corrige o filtro da compilação que excluía a tradução brasileira do APK anterior. Os textos agora são incluídos em `values-pt`, atendendo automaticamente celulares em português brasileiro e outras variantes de português. Mesmo identificador e assinatura, com versionCode 8: instale por cima da ptbr.1 para manter os roteiros e configurações. Não desinstale a versão anterior.

- Interface em português brasileiro (290 recursos, incluindo plurais e roteiro de exemplo).
- Português como idioma padrão de reconhecimento. Inglês e russo continuam disponíveis.
- Vosk offline para português, com o modelo vosk-model-small-pt-0.3 incluído no APK.
- Comparação de palavras com e sem acentos, formas coloquiais e números escritos em algarismos.
- Detecção heurística de português no modo Automático. Para roteiros curtos ou mistos, escolha o idioma manualmente.
- Aplicativo separado: `com.olerast.suflyor.ptbr`. Não substitui o Suflyor original.

## Instalação e primeiro teste

Requer Android 10 ou mais recente, processador ARM de 64 bits e espaço para o APK e a extração dos modelos. Reserve aproximadamente 400 MB.

1. Transfira o APK para o celular e abra-o para instalar.
2. Abra **Suflyor PT-BR** e permita o microfone.
3. Abra o roteiro de exemplo e confira **Fala: Português (Brasil)**.
4. Toque em **Ensaiar** e selecione o modo **Voz**. Leia o roteiro em voz alta. O primeiro carregamento extrai o modelo português e pode demorar.
5. Para usar sobre a câmera, ative o serviço de acessibilidade solicitado pelo aplicativo.
6. Se o Android bloquear essa opção, abra as informações do Suflyor PT-BR, menu ⋮, **Permitir configurações restritas**, e volte à acessibilidade.

Em um celular configurado em português, a interface acompanha o idioma automaticamente. No Android 13+, também é possível escolher Português ou Padrão do sistema nas configurações de idioma do aplicativo. Uma escolha manual de inglês ou russo no sistema prevalece sobre o idioma do celular.

## Limites desta primeira versão

O modelo português é compacto e pode errar nomes próprios, siglas, palavras estrangeiras e fala em ambiente ruidoso. O ajuste **Priorizar palavras do roteiro** funciona apenas em inglês e russo. Em português é usado o vocabulário completo, para não forçar falas fora do roteiro a coincidirem com ele.

Os testes automatizados de acompanhamento e o teste com áudio sintético não substituem uma gravação real no celular. A compatibilidade da captura simultânea com a câmera depende do aparelho. Não houve teste de microfone, câmera ou interface em um celular Android nesta entrega.

O aplicativo não traduz textos. Ele reconhece palavras e acompanha a posição no roteiro, sem enviar áudio à internet.

## Compilar

Use JDK 17 ou mais recente e Android SDK 36. Configure `sdk.dir` no arquivo local.properties.

```powershell
.\gradlew.bat testDebugUnitTest lintDebug assembleDebug
```

Para distribuição, configure uma chave própria conforme RELEASING.md e execute `assembleRelease`. A chave privada desta compilação não faz parte do pacote de código. Dependências e modelos são baixados na primeira compilação; os arquivos dos modelos têm SHA-256 fixado no Gradle. O aplicativo instalado funciona offline.

Para teste de áudio Android, a ferramenta de depuração `AsrBench` aceita `--es lang pt`, além de inglês e russo. Ela não faz parte do APK release.

## Licença e atribuição

O código mantém a licença GPL-3.0 do original; consulte LICENSE. Ao distribuir uma versão modificada, forneça também o código-fonte correspondente e os avisos de licença. Consulte THIRD_PARTY_NOTICES.md para os componentes e modelos.

O pacote de código inclui o código modificado e os scripts de compilação, sem chaves privadas, caches ou arquivos locais de configuração.
