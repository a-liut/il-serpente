package it.aliut.ilserpente.game

class MoveNotAllowedException(move: GameMove) : RuntimeException("$move move not allowed")
