package be.pxl.services.domain;

public enum PostStatus {
    DRAFT,       // Artikel in concept
    PUBLISHED,   // Gepubliceerd en zichtbaar voor gebruikers
    PENDING, // In afwachting van goedkeuring door een redacteur
    // APPROVED, // Goedgekeurd voor publicatie (optioneel, als tussenstap)
    // REJECTED, // Afgewezen door een redacteur
    // ARCHIVED // Niet langer actief maar nog wel beschikbaar als historisch item 
}
