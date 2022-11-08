import { CategoryGroupService } from "../interfaces/CategoryGroupService";

export const categoryGroupService: CategoryGroupService = {
  getCategoryGroups() {
    return Promise.resolve([
      {
        name: "Wydziały",
        totalPosts: 102,
        categories: [
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
        name: "Prowadzący",
        totalPosts: 66,
        categories: [
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
