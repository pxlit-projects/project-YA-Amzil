package be.pxl.services.domain;

public enum PostStatus {
    DRAFT,       // Artikel in concept
    PENDING_REVIEW, // In afwachting van goedkeuring door een redacteur
    APPROVED,    // Goedgekeurd voor publicatie (optioneel, als tussenstap)
    PUBLISHED,   // Gepubliceerd en zichtbaar voor gebruikers
    REJECTED,    // Afgewezen door een redacteur
    ARCHIVED     // Niet langer actief maar nog wel beschikbaar als historisch item
}
