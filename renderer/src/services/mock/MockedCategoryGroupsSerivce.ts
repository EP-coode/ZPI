import { CategoryGroupService } from "../interfaces/CategoryGroupService";

export const categoryGroupService: CategoryGroupService = {
  getCategoryGroups() {
    return Promise.resolve([
      {
        displayName: "Wydziały",
        totalPosts: 102,
        postCategories: [
          {
            displayName: "W-4n",
            totalPosts: 22,
          },
          {
            displayName: "W-8",
            totalPosts: 13,
          },
          {
            displayName: "W-12",
            totalPosts: 9,
          },
          {
            displayName: "W-1",
            totalPosts: 7,
          },
          {
            displayName: "W-2",
            totalPosts: 3,
          },
        ],
      },
      {
        displayName: "Prowadzący",
        totalPosts: 66,
        postCategories: [
          {
            displayName: "Matematycy",
            totalPosts: 32,
          },
          {
            displayName: "Fizycy",
            totalPosts: 15,
          },
          {
            displayName: "Chemicy",
            totalPosts: 9,
          },
        ],
      },
    ]);
  },
};
