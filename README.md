# SpaceInvaders
A "Space Invaders" clone taken from [this tutorial](http://www.cokeandcode.com/info/tut2d.html) with the improvements suggested at the bottom.

`shot.gif`, `ship.gif`, `alienAlt.gif`, `shotBack.gif`, and the first frames of `alien.gif` and `shotAlt.gif` all belong to Ari Feldman. The other frames of `alien.gif` and `shotAlt.gif` I knocked up in [pixlr](https://pixlr.com/), a great, free image editing software.

As it is set up at the moment, the sprites have to be put on your classpath so that the `ClassLoader` can find them. For me using intelliJ idea, this meant putting them in `out->production->[Project Title]->sprites`.
