import { CreatePostDto } from "../../dto/request/CreatePostDto";
import { Post } from "../../model/Post";
import { sleep } from "../../utils/sleep";
import { Pagination } from "../interfaces/Pagination";
import {
  PostFilters,
  PostService,
  PostsWithPagination,
} from "../interfaces/PostService";
import { userService } from "./MockedUserService";

export const postsService: PostService = {
  getPost: async function (postId: number): Promise<Post> {
    await sleep(500);
    return Promise.resolve({
      postId: postId,
      title: "Legitymacje Studenckie",
      markdownContent: `# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.
| Feature    | Support              |
| ---------: | :------------------- |
| CommonMark | 100%                 |
| GFM        | 100% w/ \`remark-gfm\` |

~~strikethrough~~

* [ ] task list
* [x] checked item

https://example.com

## Components

You can pass components to change things:

\`\`\`js
import React from 'react'
import ReactDOM from 'react-dom'
import ReactMarkdown from 'react-markdown'
import MyFancyRule from './components/my-fancy-rule.js'

ReactDOM.render(
  <ReactMarkdown
    components={{
      // Use h2s instead of h1s
      h1: 'h2',
      // Use a component instead of hrs
      hr: ({node, ...props}) => <MyFancyRule {...props} />
    }}
  >
    # Your markdown here
  </ReactMarkdown>,
  document.querySelector('#content')
)
      \`\`\``,
      category: {
        displayName: "W8",
        totalPosts: 103,
        catyegoryGroup: {
          name: "Wydziały",
          totalPosts: 320,
        },
      },
      creationTime: new Date().toISOString(),
      tags: [
        { name: "dziekanat", totalPosts: 10 },
        { name: "legitymacje", totalPosts: 13 },
      ],
      author: await userService.getUserDetails(1),
      totalDislikes: 10,
      totalLikes: 100,
      imageUrl: "https://placeimg.com/600/400/nature",
      isLiked: null,
    });
  },
  getPosts: async function (
    pagination: Pagination,
    filters?: PostFilters
  ): Promise<PostsWithPagination> {
    const posts = [];

    for (let i = 0; i < pagination.postPerPage; i++) {
      const post = await this.getPost(i);
      posts.push(post);

      if (i == pagination.postPerPage - 1) post.imageUrl = null;
    }

    const filteredPosts = posts.filter((post) =>
      filters
        ? filters.tagNames?.every((tagName) =>
            post.tags.some(
              (tag) => tag.name.toLowerCase() == tagName.toLowerCase()
            )
          ) ?? true
        : true
    );
    return {
      postCount: {
        itemsCount: filteredPosts.length + (filteredPosts.length > 0 ? 100 : 0),
      },
      posts: filteredPosts,
    };
  },
  createPost: function (post: CreatePostDto): Promise<void> {
    throw new Error("Function not implemented.");
  },
  deletePost: function (postId: string): Promise<void> {
    throw new Error("Function not implemented.");
  },
};
