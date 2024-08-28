#import "logic.typ": *

#set text(10pt, font: "Cascadia Code")
#set page(margin: 1.5cm)

#align(center, text(17pt)[ *Minesweeper* 💣 ])

#show strong: it => text(blue)[#it.body]
#show emph: it => text(purple)[#it.body]
#show smartquote: it => text(rgb("#4caf50"))[#it]

#show heading.where(level: 1): it => {
  it.body
  [ #v(10pt) ]
}
#show heading.where(level: 2): set text(10pt)

#outline(title: [Table of contents], indent: 1.5em)

#show regex("[[:^alpha:]]\d+"): set text(rgb("#4caf50"))
#show regex("[()\[\]:,\.\{\}|]"): set text(rgb("#4caf50"))
#show regex("[a-zA-Zàèìù]+\("): set text(blue)
#show regex("this|result|auth|adesso|old|new"): set text(blue)
#show regex("True"): set text(rgb("#4caf50"))

#pagebreak()

#page(margin: 20pt, flipped: true)[
  = #text(weight: "extrabold")[UML]
  #align(
    center + horizon, 
    image("Minesweeper.png", width: 100%)
  )
]

= *Game* class specification

#constraint(
  [*Game*._at_most_one_uncovered_mine_],
  [
    $forall$ game \
    #t Game(game) ==> \
    #t#t $not$ $exists$ m1, m2 \
    #t#t#t m1 != m2 $and$ \
    #t#t#t _mine_game_(m1, game) $and$ \
    #t#t#t _mine_game_(m2, game) $and$ \
    #t#t#t Revealed(m1) $and$ \
    #t#t#t Revealed(m2) \
  ]
)

#constraint(
  [*Game*._victory_condition_],
  [
    $forall$ game\
    #t Victory(game) <=> \
    #t#t $forall$ tile _mine_game_(tile, game) #[==>] Flagged(tile) $and$ \
    #t#t $not$ $exists$ tile _tile_game_(tile, game) $and$ Empty(tile) $and$ Flagged(tile) \
    #t#t $or$ \
    #t#t $forall$ tile (_tile_game_(tile, game) $and$ Empty(tile) #[==>] Revealed(tile)) \
  ]
) 

#constraint(
  [*Game*._loss_condition_],
  [
    $forall$ game \
    #t Loss(game) <=> $exists$ mine _mine_game_(mine, game) $and$ Revealed(mine)
  ]
)

#constraint(
  [*Game*._only_one_uncompleted_game_],
  [
    $forall$ g1, g2 \
    #t Game(g1) $and$ Game(g2) $and$ g1 != g2 ==> \ 
    #t#t Ended(g1) $or$ Over(g2)
  ]
)

#operation(
  [mines],
  type: [(0..100)],
  post: [
    result = |{ mine | _mine_game_(mine, this) }|
  ]
)

#operation(
  [flags],
  type: [(0..100)],
  post: [
    result = |{ flag | _tile_game_(flag, this) $and$ Flag(tile) }|
  ]
)

#pagebreak()

= *Tile* class specification

#operation(
  [is_adjacent],
  args: [tile: *Tile*],
  type: [*Boolean*],
  post: [
    result = True <=> $exists$ game, x, y \
    #t _tile_game_(this, game) $and$ \
    #t _tile_game_(tile, game) $and$ \
    #t x(this, x) $and$ \
    #t y(this, y) $and$ \
    #t (\
    #t#t (x(tile, x - 1) $and$ y(tile, y - 1)) $or$ \
    #t#t (x(tile, x ) $and$ y(tile, y - 1)) $or$ \
    #t#t (x(tile, x + 1) $and$ y(tile, y - 1)) $or$ \
    #t#t (x(tile, x - 1) $and$ y(tile, y + 1)) $or$ \
    #t#t (x(tile, x ) $and$ y(tile, y + 1)) $or$ \
    #t#t (x(tile, x + 1) $and$ y(tile, y + 1)) $or$ \
    #t#t (x(tile, x - 1) $and$ y(tile, y)) $or$ \
    #t#t (x(tile, x + 1) $and$ y(tile, y)) $or$ \
    #t ) \
  ]
)

= *Empty* class specification

#constraint(
  [*Empty*._revealed_empty_tile_reveals_adjacents_],
  [
    $forall$ safe, game, adjacent \
    #t _tile_game_(safe, game) $and$ \
    #t Empty(safe) $and$ \
    #t Revealed(safe) $and$ \
    #t *is_adjacent*#sub[*Tile*, *Tile*, *Boolean*]\(safe, adjacent, True) $and$ \
    #t *adjacent_mines*#sub[*Empty*, (0..8)]\(safe, 0) ==> \
    #t#t Revealed(adjacent)
  ]
)


#operation(
  [adjacent_mines],
  type: [(0..8)],
  post: [
    result = |{ mine | $exists$ game \
    #t _tile_game_(this, game) $and$ \
    #t _mine_game_(mine, game) $and$ \
    #t *is_adjacent*#sub[*Tile*, *Tile*, *Boolean*]\(this, mine, True) \
    }| \
  ]
)

#pagebreak()

= #text(weight: "extrabold")[Use Case]

#operation(
  [start_game],
  type: [*Game*],
  pre: [
    $not$ $exists$ game Game(game) $and$ $not$ Ended(game)
  ],
)

#operation(
  [terminate_game],
  args: [game: *Game*],
  type: [*Terminated*],
  pre: [
    $not$ Ended(game)
  ],
)

#operation(
  [reveal],
  args: [tile: *Hidden*],
  type: [*Revealed*],
  pre: [
    $exists$ game _tile_game_(tile, game) ==> $not$ Ended(game)
  ]
)

#operation(
  [flag],
  args: [tile: *Hidden*],
  type: [*Flagged*],
  pre: [
    $exists$ game _tile_game_(tile, game) ==> $not$ Ended(game)
  ]
)

#operation(
  [remove_flag],
  args: [tile: *Flagged*],
  type: [*Hidden*],
  pre: [
    $exists$ game _tile_game_(tile, game) ==> $not$ Ended(game) 
  ]
)

#operation(
  [games_played],
  args: [],
  type: [*Integer* >= 0],
  post: [
    result = |{ game | Game(game) }|
  ]
)

#operation(
  [games_won],
  args: [],
  type: [*Integer* >= 0],
  post: [
    result = |{ game | Victory(game) }|
  ]
)

#page(margin: 20pt, flipped: true)[
  = #text(weight: "extrabold")[Wireframe]
  #align(
    center + horizon, 
    image("wireframe.png", width: 89%)
  )
]