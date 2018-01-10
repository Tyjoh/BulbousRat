# BulbousRat
Game engine using libgdx and the Artemis entity component system. Comparable to how Unity GameObjects use compoonents to define them. [Video Demo](https://youtu.be/3pUCTBitfiU)

## Significant current features:
* Built on top of libGDX to handle graphics and basic game functions like i/o.
* Uses Artemis ECS as the main architecture of the game
  * ECS used as an alternative to a heirarchical or more traditional oop approach to games. Increases encapsulation, better cache performance, quicker to add features, etc.
  * Entities or GameObejcts are just a integer.
  * Components contain only data and are assigned to entities as needed, the functionality of the entity is defined by the components it possesses.
  * Systems define the functionality and are run on entities with a certain set of components that the system requires to run.
* World and Enties are defined through json files and is intended to have a corresponding editor to make this easier.
* Uses the Box2D java port for the physics engine. Handles collisions, joints, velocity... etc.
* Game events posted to the event buss or event blackboard and systems can check for certain events concerning certain entities or events concerning the entire world.
* Very customizable input/control scheme to allow for custom bindings and ease of new types of controllers to be supported in the future.
