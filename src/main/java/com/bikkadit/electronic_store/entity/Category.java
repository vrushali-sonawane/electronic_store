package com.bikkadit.electronic_store.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="categories")
public class Category {

    @Id
    private String categoryId;

    @Column(name="category_title")
    private String title;
    @Column(name="category_description")
    private String categoryDescription;
    private String coverImage;
}
