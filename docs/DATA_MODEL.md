# DovahCheck data model

The master quest catalog is immutable game/reference data. Player progress is stored separately per character.

## Core identity

Every catalog record has an app-owned `internalKey`. Display titles are not keys because Skyrim reuses quest names across Holds, radiant templates, branches and DLC.

## Completion handling

- One-time quest: count once.
- Infinite radiant template: count once after the player completes one instance.
- Finite repeatable: store `requiredIterations` and count complete only after that number is reached.
- Route alternative: count the player's reachable chosen route rather than requiring mutually exclusive content.
- Source duplicate: retain for audit but exclude from the completion denominator.
- Hearthfire systems: tracked in a dedicated completion panel rather than treated as ordinary journal quests.

## Character progress

Character state must reference catalog records by `internalKey`. It must never edit the catalog record itself.

Planned persistence model:

- CharacterProfile
- QuestProgress
- CollectionProgress
- AppSettings

## Spoilers

Quest data is designed for layered disclosure:

1. title/status only
2. hint
3. more detail
4. full solution

The initial imported data only contains spoiler-light summaries. Walkthrough text is intentionally not part of the foundation dataset.
