# DovahCheck collections

Collections are tracked per character and are intentionally separate from quest progress.

## Shouts v1

The first live collection module contains:

- 27 Dragon Shouts
- 81 individual Words of Power
- Base Game, Dawnguard and Dragonborn release filtering
- Per-word learned state
- Per-shout completion derived from all three words
- Acquisition/location notes from the source inventory

Collection progress is stored using a stable collection key plus item key. The shout implementation uses:

- collection key: `shout_words`
- item key: `shout:<shout-slug>:<word-slug>`

This structure is reusable for masks, artifacts, stones, Black Books, Standing Stones and other finite sets without mixing them into the quest table.
