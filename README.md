# Hypnos

Minecraft mod using fabric api

## Config

### Values

- `playerPercentage` is the percentage of player that need to sleep
- `notEnoughPlayerMessage` is the message displayed when a player enter his bed but not enough players are sleeping
- `nightSkipMessage` is the message used when skipping night
- `enableWakeUp` enable or disable broadcasting of `wakeUpMessage`
- `wakeUpMessage` is the message displayed when a player leaves his bed
- `ignoreSleepDuringDay` enabling it will ignore when player enter a bed during day (true per default)

### Message arguments

In messages, you can use different variables

- `{player}`: player entering his bed
- `{sleeping}`: amount of players sleeping
- `{required}`: number of required player sleeping to skip night

Each message can have only specific arguments
- `notEnoughPlayerMessage`: `{player}`, `{sleeping}`, `{required}`
- `nightSkipMessage`: `{sleeping}`, `{required}`
- `wakeUpMessage`: `{player}`, `{sleeping}`, `{required}`

You can also use minecraft color tags as seen [here](https://minecraft.gamepedia.com/Formatting_codes)

### Default config

```json
{
  "playerPercentage": 50,
  "notEnoughPlayerMessage": "§9{player} entered bed, not enought to skip night [{sleeping}/{required}]§r",
  "nightSkipMessage": "§9Enough players are sleeping, skipping night§r",
  "wakeUpMessage": "§9{player} leaved bed§r",
  "enableWakeUp": false,
  "ignoreSleepDuringDay": true
}
```