import { Tag } from "../../model/Tag";
import { sleep } from "../../utils/sleep";
import { TagsService } from "../interfaces/TagsService";

export const tagsService: TagsService = {
  getTagsByPrefix: async function (
    prefix: string,
    limit: number
  ): Promise<{ tags: Tag[] }> {
    
    const exampleTags: Tag[] = [
      { name: "Legitymacje", totalPosts: 32 },
      { name: "Akademiki", totalPosts: 32 },
      { name: "Dziekanat", totalPosts: 32 },
      { name: "Jedzenie", totalPosts: 32 },
      { name: "Jsos", totalPosts: 32 },
      { name: "Usos", totalPosts: 32 },
      { name: "Koła naukowe", totalPosts: 32 },
      { name: "Humor", totalPosts: 32 },
    ];

    await sleep(1000)

    return Promise.resolve({
      tags: exampleTags.filter((tag) => tag.name.toLowerCase().startsWith(prefix.toLowerCase())),
    });
  },
  addTag: function (tagName: string): Promise<void> {
    throw new Error("Function not implemented.");
  },
};
