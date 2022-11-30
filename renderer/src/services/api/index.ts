export { postsService } from "./PostService";
export { categoryGroupService } from "./CategoryGroupsService";
export { tagsService } from "./TagsService";
export { userService } from "./UserService"

export const DATA_PROVIDER_URL =
  process.env.DATA_PROVIDER_URL ?? "http://localhost:8080";
