# BedWars189

Plugin de BedWars para servidor Spigot 1.8.8, para jogadores com Minecraft 1.8.9. O projeto também inclui arquivos básicos para iniciar um servidor local. Ainda é um protótipo.

## Recursos implementados

- Times `red` e `blue`.
- Entrar e sair da partida.
- Configuração das posições de spawn e das camas.
- Início da partida e anúncio de vitória quando uma cama é destruída.
- Permissão administrativa `bedwars.admin`.

## Comandos

| Comando | Uso |
|---|---|
| `/bw join red` | Entrar no time vermelho |
| `/bw join blue` | Entrar no time azul |
| `/bw leave` | Sair do time |
| `/bw setspawn red` | Salvar sua posição como spawn vermelho |
| `/bw setspawn blue` | Salvar sua posição como spawn azul |
| `/bw setbed red` | Mirar no bloco da cama vermelha e salvar |
| `/bw setbed blue` | Mirar no bloco da cama azul e salvar |
| `/bw start` | Iniciar a partida |

Os comandos `setspawn`, `setbed` e `start` exigem `bedwars.admin`.

## Métodos principais

O código fica em `src/main/java/br/com/bedwars189/BedWars.java`.

- `onEnable()`: prepara times, carrega camas e spawns salvos e registra os eventos.
- `onCommand(...)`: trata os comandos `/bw`.
- `leave(Player)`: remove o jogador do time atual.
- `onBreak(BlockBreakEvent)`: verifica a destruição de uma cama inimiga e anuncia o vencedor.
- `onDeath(PlayerDeathEvent)`: trata jogador morto quando seu time já perdeu a cama.
- `onRespawn(PlayerRespawnEvent)`: envia jogadores de times ainda vivos ao spawn do time.

## Compilar o plugin

Requisitos: Java 8 e Maven.

Na pasta do projeto:

```powershell
mvn package
```

O JAR será gerado em `target/BedWars189.jar`. Copie-o para a pasta `plugins` do seu servidor Spigot 1.8.8 e reinicie o servidor.

## Servidor local e conexão

Veja [COMO-CONECTAR.txt](COMO-CONECTAR.txt). O arquivo `server.properties` deixa `server-ip` vazio e usa a porta padrão 25565. Isso não cria um IP público: para acesso pela Internet é necessário hospedar o servidor e configurar a rede/porta.

## Bots PvP

Para NPCs de combate, use versões de Citizens e Sentinel compatíveis com a sua build 1.8.8. Este projeto não implementa a IA dos bots nem inclui esses plugins.

## Limitações

Sem loja, geradores de recursos, proteção do mapa, balanceamento, suporte a múltiplas arenas ou gerenciamento completo de respawn/eliminação. Não use como servidor público pronto para produção sem implementar e revisar esses sistemas.

## Publicar no GitHub

Crie um repositório vazio no GitHub, sem gerar README ou licença, depois rode estes comandos dentro desta pasta (substitua a URL pela URL do seu repositório):

```powershell
git init
git add .
git commit -m "Initial BedWars189 project"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
git push -u origin main
```

