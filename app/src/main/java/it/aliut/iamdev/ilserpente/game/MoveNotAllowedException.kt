package it.aliut.iamdev.ilserpente.game

class MoveNotAllowedException(move: GameMove) : RuntimeException("$move move not allowed")