package com.kanbee.project.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanbee.project.model.HotPlace;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AiRecommendationService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    private final String[] themes = {"Modern", "Traditional", "Hidden Gems", "Instagrammable", "Local Favorites", "Luxurious", "Budget-friendly", "Nature-focused", "Nightlife", "Unique Experience"};
    
    // Fallback Image List (High-quality images from Unsplash)
    private final String[] fallbackImages = {
        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80", // Restaurant/Cafe
        "https://images.unsplash.com/photo-1559339352-11d035aa65de?auto=format&fit=crop&w=800&q=80", // Interior
        "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80", // Food
        "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?auto=format&fit=crop&w=800&q=80", // Beauty/Salon
        "https://images.unsplash.com/photo-1585478259715-876acc5be8eb?auto=format&fit=crop&w=800&q=80", // Bagel/Cafe
        "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?auto=format&fit=crop&w=800&q=80", // Chicken/Food
        "https://images.unsplash.com/photo-1583209814683-c023dd293cc6?auto=format&fit=crop&w=800&q=80", // Hanbok/Activity
        "https://images.unsplash.com/photo-1538485399081-7191377e8241?auto=format&fit=crop&w=800&q=80", // Seoul City
        "https://images.unsplash.com/photo-1535189043414-47a3c49a0bed?auto=format&fit=crop&w=800&q=80", // Seoul Night
        "https://images.unsplash.com/photo-1610444663701-85684605e783?auto=format&fit=crop&w=800&q=80"  // Cafe/Drink
    };

    public AiRecommendationService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<HotPlace> getRecommendations(String category) {
        // 1. Google Gemini API (Free Tier) 우선 시도
        if (geminiApiKey != null && !geminiApiKey.isEmpty() && !"YOUR_GEMINI_KEY_HERE".equals(geminiApiKey)) {
            return getGeminiRecommendations(category);
        }

        // 2. OpenAI API (Paid) 시도
        if (openAiApiKey != null && !openAiApiKey.isEmpty() && !"YOUR_OPENAI_KEY_HERE".equals(openAiApiKey)) {
            return getOpenAiRecommendations(category);
        }

        return new ArrayList<>();
    }

    private List<HotPlace> getGeminiRecommendations(String category) {
        try {
            String theme = themes[random.nextInt(themes.length)];
            String prompt = String.format(
                "Recommend 3 unique and distinct popular places in Seoul for category '%s' with a focus on '%s' theme. " +
                "Do not repeat the same places if asked again. " +
                "Return ONLY a raw JSON array of objects with these fields: name, description (short summary in Korean), location (address or area), tag (hashtags like #Coffee #View). " +
                "Ensure the response is valid JSON. Do not include markdown formatting or '```json' wrapper.", category, theme);

            // Gemini API Request Structure
            String requestBody = String.format(
                "{\"contents\": [{\"parts\": [{\"text\": \"%s\"}]}]}", prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent?key=" + geminiApiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String content = root.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
                
                return parseJsonContent(content, category);
            } else {
                System.err.println("Gemini API Error: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<HotPlace> getOpenAiRecommendations(String category) {
        try {
            String prompt = String.format(
                "Recommend 3 popular places in Seoul for category '%s'. " +
                "Return a JSON array of objects with fields: name, description (short summary in Japanese), location (address or area), tag (hashtags like #Coffee #View). " +
                "Do not include any markdown formatting, just raw JSON.", category);

            String requestBody = objectMapper.writeValueAsString(new OpenAiRequest("gpt-3.5-turbo", prompt));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiApiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode root = objectMapper.readTree(response.body());
                String content = root.path("choices").get(0).path("message").path("content").asText();
                return parseJsonContent(content, category);
            } else {
                System.err.println("OpenAI API Error: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<HotPlace> parseJsonContent(String content, String category) {
        List<HotPlace> recommendations = new ArrayList<>();
        try {
            // Clean up JSON string (remove markdown and find array)
            content = content.replaceAll("```json", "").replaceAll("```", "").trim();
            int start = content.indexOf("[");
            int end = content.lastIndexOf("]");
            if (start != -1 && end != -1) {
                content = content.substring(start, end + 1);
            }
            
            JsonNode root = objectMapper.readTree(content);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    HotPlace place = new HotPlace();
                    place.setName(node.path("name").asText());
                    place.setDescription(node.path("description").asText());
                    place.setLocation(node.path("location").asText());
                    place.setTag(node.path("tag").asText());
                    place.setCategory(category);
                    
                    // Assign random image if missing
                    if (node.has("imageUrl") && !node.path("imageUrl").asText().isEmpty()) {
                        place.setImageUrl(node.path("imageUrl").asText());
                    } else {
                        place.setImageUrl(fallbackImages[random.nextInt(fallbackImages.length)]);
                    }
                    
                    recommendations.add(place);
                }
            }
        } catch (Exception e) {
            System.err.println("AI Response Parsing Error: " + e.getMessage());
            e.printStackTrace();
        }
        return recommendations;
    }

    // Inner classes for OpenAI request structure
    private static class OpenAiRequest {
        public String model;
        public List<Message> messages;

        public OpenAiRequest(String model, String prompt) {
            this.model = model;
            this.messages = new ArrayList<>();
            this.messages.add(new Message("user", prompt));
        }
    }

    private static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
