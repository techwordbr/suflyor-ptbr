# Suflyor PT-BR

Teleprompter para Android que acompanha sua leitura em português, sem internet.

**Versão modificada independente mantida pelo Tech Word BR**, baseada no [Suflyor de olerast67](https://github.com/olerast67/voice-teleprompter-android). Não é uma versão oficial nem possui vínculo de aprovação com o autor original.

## Baixar e instalar

**[Baixar o APK e o código-fonte da versão 0.7-ptbr.2](https://github.com/techwordbr/suflyor-ptbr/releases/tag/v0.7-ptbr.2)**

- Android 10 ou mais recente, com processador ARM de 64 bits.
- APK de aproximadamente 175 MB, com os modelos de voz incluídos. Reserve cerca de 400 MB para instalação e uso.
- Abra o APK no celular e permita a instalação pelo aplicativo usado para abrir o arquivo.
- Se já instalou a nossa versão PT-BR, **atualize sem desinstalar** para manter os roteiros. Ela instala separadamente do Suflyor original.

## Comece por aqui

1. Abra Suflyor PT-BR e permita o microfone.
2. Crie ou importe um roteiro em português.
3. Confira **Fala: Português (Brasil)**, toque em **Ensaiar** e selecione **Voz**.
4. Leia em voz alta; o texto acompanha as palavras reconhecidas. O primeiro carregamento pode demorar enquanto o modelo é extraído.
5. Para gravar com a janela sobre a câmera, ative o serviço de acessibilidade solicitado pelo aplicativo.

No Android 13 ou posterior, caso a acessibilidade esteja bloqueada: Configurações → Aplicativos → Suflyor PT-BR → ⋮ → Permitir configurações restritas. Depois volte à acessibilidade.

A interface acompanha o idioma do celular quando está em português. Se você escolheu manualmente outro idioma para o aplicativo, selecione **Padrão do sistema** ou **Português** nas configurações do Android.

## O que esta versão acrescenta

- Reconhecimento de português offline com Vosk e o modelo compacto Alpha Cephei.
- Interface em português: 290 recursos traduzidos, incluindo plurais e mensagens de erro.
- Português selecionado como idioma padrão da fala.
- Comparação de palavras com acentos, formas coloquiais e tratamento de números no roteiro.
- Inglês e russo continuam disponíveis.
- A ptbr.2 corrige a inclusão dos textos em português no APK, removidos por um filtro na ptbr.1.

O aplicativo reconhece a fala e acompanha o roteiro; não traduz textos entre idiomas. O APK não solicita permissão de internet.

## Limitações e testes

O modelo português pode errar nomes próprios, siglas, palavras estrangeiras e fala em ambiente ruidoso. O ajuste de priorização de palavras do roteiro é usado somente pelos modelos de inglês e russo. A captura simultânea com a câmera depende do Android e do fabricante.

Foram executados 95 testes automatizados na implementação PT-BR, além de teste com voz sintética brasileira. Na ptbr.2, foi conferida a presença dos 290 recursos traduzidos **dentro do APK final**, a assinatura e a compatibilidade de atualização. Houve relato de funcionamento da fala em um celular do mantenedor; isso não representa validação em todos os modelos de aparelho.

## Código-fonte e compilação

O código correspondente ao APK está na tag **v0.7-ptbr.2** e no ZIP anexado à mesma Release. O código original foi preservado no histórico Git, a partir do commit `20a2f73f881e15ba373d714e53795e993971e3d0` (Suflyor 0.7).

Use JDK 17 ou mais recente e Android SDK 36. Crie `local.properties` com o caminho do SDK:

```properties
sdk.dir=/caminho/para/Android/Sdk
```

```sh
./gradlew assembleDebug testDebugUnitTest lintDebug
```

No Windows, use `gradlew.bat`. Bibliotecas e modelos são baixados na primeira compilação. Os downloads dos modelos e do sherpa-onnx têm SHA-256 fixado no Gradle. O aplicativo instalado funciona offline. Consulte [README.pt-BR.md](README.pt-BR.md), [RELEASING.md](RELEASING.md) e [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).

## Créditos e licença

O aplicativo original é de [olerast67 e colaboradores](https://github.com/olerast67/voice-teleprompter-android). As alterações PT-BR foram feitas em 1 de novembro de 2026; a documentação de publicação foi organizada em 2 de novembro de 2026. Os avisos e créditos originais são mantidos no histórico e em [README.upstream.md](README.upstream.md).

O código desta versão modificada permanece sob **[GNU GPL versão 3](LICENSE)**. Você pode estudar, modificar e redistribuir, respeitando a licença e fornecendo o código-fonte correspondente ao distribuir binários. Não há garantia. Bibliotecas e modelos mantêm suas próprias licenças: veja [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md) e [licenses/](licenses/).

Problemas desta versão: abra uma [issue neste repositório](https://github.com/techwordbr/suflyor-ptbr/issues), informando a versão, o modelo do aparelho e como reproduzir. Não publique informações pessoais ou gravações privadas.
