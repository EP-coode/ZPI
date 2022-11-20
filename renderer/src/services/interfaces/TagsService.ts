import { Tag } from "../../model/Tag";

export interface TagsService {
  getTagsByPrefix(prefix: string, limit: number): Promise<{ tags: Tag[] }>;
  addTag(tagName: string): Promise<void>;
}
