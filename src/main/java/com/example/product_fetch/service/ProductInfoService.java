package com.example.product_fetch.service;

import com.example.product_fetch.model.ProductInfo;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductInfoService {
    private static final Logger logger = LoggerFactory.getLogger(ProductInfoService.class);

    @Autowired
    private WebScraperService webScraperService;

    public ProductInfo extractProductInfo(String url) {
        ProductInfo productInfo = new ProductInfo();

        try {
            Document doc = webScraperService.scrapeWebPage(url);

            // More flexible selectors with fallback mechanisms
            productInfo.setName(extractText(doc,
                    "h1.product-title",
                    "h1#product-name",
                    ".product-name",
                    "title"
            ));

            productInfo.setPrice(extractText(doc,
                    "span.price",
                    "div.price",
                    ".product-price",
                    "#price"
            ));

            productInfo.setDescription(extractText(doc,
                    "div.product-description",
                    "#description",
                    ".description",
                    "meta[name='description']"
            ));

            productInfo.setImageUrl(extractImageUrl(doc));

            // Log extracted information for debugging
            logger.info("Extracted Product Info: {}", productInfo);

            return productInfo;
        } catch (Exception e) {
            logger.error("Error extracting product information from URL: {}", url, e);
            throw new RuntimeException("Could not extract product information", e);
        }
    }

    // Flexible text extraction method
    private String extractText(Document doc, String... selectors) {
        for (String selector : selectors) {
            Elements elements = doc.select(selector);
            if (!elements.isEmpty()) {
                Element element = elements.first();

                // Different extraction strategies based on element type
                switch (selector) {
                    case "meta[name='description']":
                        return element.attr("content");
                    case "title":
                        return element.text();
                    default:
                        return element.text();
                }
            }
        }
        return "Not Available";
    }

    // Flexible image URL extraction
    private String extractImageUrl(Document doc) {
        String[] imageSelectors = {
                "img.product-image",
                ".product-image img",
                "#product-image",
                "img[itemprop='image']",
                "meta[property='og:image']"
        };

        for (String selector : imageSelectors) {
            Elements images = doc.select(selector);
            if (!images.isEmpty()) {
                Element image = images.first();

                // Different attribute extraction based on selector
                switch (selector) {
                    case "meta[property='og:image']":
                        return image.attr("content");
                    default:
                        return image.attr("src");
                }
            }
        }
        return "Image Not Found";
    }
}