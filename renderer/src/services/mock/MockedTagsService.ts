import { Tag } from "../../model/Tag";
import { sleep } from "../../utils/sleep";
import { TagsService } from "../interfaces/TagsService";

export const tagsService: TagsService = {
  getTagsByPrefix: async function (
    prefix: string,
    limit: number
  ): Promise<{ tags: Tag[] }> {
    const exampleTags: Tag[] = [
      { tagName: "Legitymacje", totalPosts: 32 },
      { tagName: "Akademiki", totalPosts: 32 },
      { tagName: "Dziekanat", totalPosts: 32 },
      { tagName: "Jedzenie", totalPosts: 32 },
      { tagName: "Jsos", totalPosts: 32 },
      { tagName: "Usos", totalPosts: 32 },
      { tagName: "Koła naukowe", totalPosts: 32 },
      { tagName: "Humor", totalPosts: 32 },
    ];

    await sleep(1000);

    return Promise.resolve({
      tags: exampleTags.filter((tag) =>
        tag.tagName.toLowerCase().startsWith(prefix.toLowerCase())
      ),
    });
  }
};
