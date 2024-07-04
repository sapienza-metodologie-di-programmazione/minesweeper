#import "logic.typ": *

// TODO: forchette
// #set text(10pt, font: "CaskaydiaCove NF")
#set text(10pt, font: "Cascadia Code")
// #show math.equation: set text(font: "CaskaydiaCove NF")
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
// #show regex("[\*\d]+"): set text(rgb("#4caf50"))
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

// = Data types specification 

// #tile(
//   inset: 5pt,
//   [
//     *Username*: *String* according to regex `[\w_][\w\d_]+`
//   ]
// )

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

// == *Play* use case specification

// #let mantra = [Let *user* be the instance of *User* associated with the invoking actor] 

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

// holy nation


// // #image("Use Case RistoBook.png", width: 100%)

// #pagebreak()

// = Specifica use case *Cucine*

// #signature([inserisci_cucina], args: [nome: *Stringa*], type: [*Cucina*])
// #signature([elenca_cucine], type: [*Cucina* [0..\*]])

// = Specifica use case *Ristorante*

// #signature(
//   [registra_ristorante],
//   args: [nome: *Stringa*, partitaIVA: *PartitaIVA*, indirizzo: *Indirizzo*, città: *Città*, cucine: *Cucina* [1..\*]],
//   type: [*Ristorante*]
// )

// = Specifica use case *Promozioni*

// #signature(
//   [inserisci_promozione],
//   args: [sconto: *Sconto*, max_coperti: *Intero* > 0 [0..1], fasce: *FasciaOraria* [1..\*]],
//   type: [*Promozione*]
// )

// = Specifica use case *Chiusure*

// #signature([inserisci_chiusura], args: [fascia: *FasciaOraria*])
// #signature([riapri_chiusura], args: [fascia: *FasciaOraria*])

// = Specifica use case *Gestione prenotazioni*

// #signature([conferma], args: [prenotazione: *Prenotazione*], type: [*Confermata*])
// #signature([rifiuta], args: [prenotazione: *Prenotazione*], type: [*Rifiutata*])
// #signature([completa], args: [confermata: *Confermata*], type: [*Completata*])
// #signature([non_usata], args: [confermata: *Confermata*], type: [*NonUsata*])

// = Specifica use case *Effettuazione prenotazioni*

// #signature(
//   [prenota], 
//   args: [orario: *DataOra*, commensali: *Intero* > 0, ristorante: *Ristorante*, promozione: *Promozione* [0..1]],
//   type: [*Prenotazione*]
// )
// #signature(
//   [annulla],
//   args: [prenotazione: *Prenotazione*],
//   type: [*Annullata*]
// )

// #pagebreak()

// = Specifica use case *Statistiche*

// #operation(
//   [utilizzo_promozioni],
//   args: [dal: *Data*, al: *Data*],
//   type: [(*Promozione*, *Reale* >= 0)],
//   post: [
//     result = { ($psi$, $mu$) | \
//     #t Promozione($psi$) $and$ \
//     #t T = {($delta$, prenotazioni) | $exists$ $phi$, i, $delta_i$, f, $delta_f$ \
//     #t#t Data($delta$) $and$ \
//     #t#t dal <= $delta$ <= al $and$ \
//     #t#t fasce($psi$, $phi$) $and$ \
//     #t#t inizio($phi$, i) $and$ \
//     #t#t data(inizio, $delta_i$) $and$ \
//     #t#t fine($phi$, f) $and$ \
//     #t#t data(fine, $delta_f$) $and$ \
//     #t#t $delta_i$ <= $delta$ <= $delta_f$ \
//     #t#t prenotazioni = |{ p | $exists$ o \
//     #t#t#t _prenotazione_promozione_(p, $psi$) $and$ \
//     #t#t#t orario(p, o) $and$ \
//     #t#t#t i <= o <= f \
//     #t#t }| \
//     #t} \

//     #t |T| > 0 ==> $mu$ = $(sum_((\_, p) in T) p) / (|T|)$ $and$\
//     #t |T| = 0 ==> $mu$ = 0 \
//     }
//   ]

// )

// = Specifica use case *Ricerca ristoranti*

// #operation(
//   [ricerca_ristoranti],
//   args: [x: *Città*, C: *Cucina* [1..\*], s: *Sconto*, $delta$: *Data*, coperti: *Intero* > 0],
//   type: [*Ristorante* [0..\*]],
//   post: [
//     result = { ristorante | \
//     #t _città_ristorante_(x, ristorante) $and$ \
//     #t $exists$ $psi$, $phi$, m, i, $delta_i$, f, $delta_f$ \
//     #t#t _promozione_ristorante_($psi$, r) $and$ \
//     #t#t fasce($psi$, $phi$) $ and$ \
//     #t#t inizio($phi$, i) $and$ \
//     #t#t data(i, $delta_i$) $and$ \
//     #t#t fine($phi$, f) $and$ \
//     #t#t data(f, $delta_f$) $and$ \
//     #t#t $delta_i$ <= $delta$ <= $delta_f$ $and$ \
//     \
//     #t#t *max_coperti*($psi$, m) $and$ \
//     #t#t Sia P = {(p, c) |  _prenotazione_promozione_(p, $psi$) $and$ commensali(p, c)} \
//     #t#t m - $sum$#sub(typographic: false, size: 1em, baseline: 1.2em)[(\_, c) $in$ P] c >= coperti
//     \
//     \
//     }
//   ]
// )
