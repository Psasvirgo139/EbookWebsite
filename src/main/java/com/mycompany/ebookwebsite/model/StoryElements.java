package com.mycompany.ebookwebsite.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📖 Story Elements Model
 * 
 * Lưu trữ các thành phần đã phân tích của câu chuyện
 */
public class StoryElements {
    
    private List<Character> characters;
    private String plot;
    private List<String> themes;
    private String style;
    private List<String> highlights;
    private Map<String, Object> additionalInfo;
    
    public StoryElements() {
        this.characters = new ArrayList<>();
        this.themes = new ArrayList<>();
        this.highlights = new ArrayList<>();
        this.additionalInfo = new HashMap<>();
    }
    
    // Character inner class
    public static class Character {
        private String name;
        private String description;
        private String personality;
        private String role;
        
        public Character() {}
        
        public Character(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        public String getPersonality() { return personality; }
        public void setPersonality(String personality) { this.personality = personality; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        @Override
        public String toString() {
            return "Character{name='" + name + "', role='" + role + "'}";
        }
    }
    
    // Getters and Setters
    public List<Character> getCharacters() {
        return characters;
    }
    
    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }
    
    public String getPlot() {
        return plot;
    }
    
    public void setPlot(String plot) {
        this.plot = plot;
    }
    
    public List<String> getThemes() {
        return themes;
    }
    
    public void setThemes(List<String> themes) {
        this.themes = themes;
    }
    
    public String getStyle() {
        return style;
    }
    
    public void setStyle(String style) {
        this.style = style;
    }
    
    public List<String> getHighlights() {
        return highlights;
    }
    
    public void setHighlights(List<String> highlights) {
        this.highlights = highlights;
    }
    
    public Map<String, Object> getAdditionalInfo() {
        return additionalInfo;
    }
    
    public void setAdditionalInfo(Map<String, Object> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
    
    // Helper methods
    public void addCharacter(String name, String description, String role) {
        Character character = new Character(name, description);
        character.setRole(role);
        this.characters.add(character);
    }
    
    public void addTheme(String theme) {
        if (theme != null && !theme.trim().isEmpty()) {
            this.themes.add(theme.trim());
        }
    }
    
    public void addHighlight(String highlight) {
        if (highlight != null && !highlight.trim().isEmpty()) {
            this.highlights.add(highlight.trim());
        }
    }
    
    public Character findCharacterByName(String name) {
        return characters.stream()
            .filter(c -> c.getName() != null && c.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    public boolean hasCharacters() {
        return !characters.isEmpty();
    }
    
    public boolean hasThemes() {
        return !themes.isEmpty();
    }
    
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (!characters.isEmpty()) {
            summary.append("Nhân vật: ").append(characters.size()).append(" người. ");
        }
        
        if (!themes.isEmpty()) {
            summary.append("Chủ đề: ").append(String.join(", ", themes)).append(". ");
        }
        
        if (style != null && !style.trim().isEmpty()) {
            summary.append("Phong cách: ").append(style).append(".");
        }
        
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return "StoryElements{" +
               "characters=" + characters.size() +
               ", themes=" + themes.size() +
               ", style='" + style + '\'' +
               ", highlights=" + highlights.size() +
               '}';
    }
} 