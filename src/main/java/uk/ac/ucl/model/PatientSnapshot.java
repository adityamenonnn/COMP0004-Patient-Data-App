package uk.ac.ucl.model;

import java.util.Map;

// RECORD in this case
// Pairs a patient's field data with its DataFrame row index so callers
// don't have to carry the index separately alongside a raw Map.
public record PatientSnapshot(int rowIndex, Map<String, String> data) {}