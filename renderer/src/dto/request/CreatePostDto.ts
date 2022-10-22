export interface CreatePostDto {
  title: string;
  markdownContent: string;
  categoryName: string;
  tagNames: string[];
}
