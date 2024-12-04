package be.pxl.services.domain;

public enum PostStatus {
    DRAFT,       // Artikel in concept
    PUBLISHED,   // Gepubliceerd en zichtbaar voor gebruikers
    // PENDING_REVIEW, // In afwachting van goedkeuring door een redacteur
    // APPROVED, // Goedgekeurd voor publicatie (optioneel, als tussenstap)
    // REJECTED, // Afgewezen door een redacteur
    // ARCHIVED // Niet langer actief maar nog wel beschikbaar als historisch item 
}
