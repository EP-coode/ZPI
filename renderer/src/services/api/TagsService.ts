import { DATA_PROVIDER_URL } from ".";
import { Tag } from "../../model/Tag";
import { TagsService } from "../interfaces/TagsService";

export const tagsService: TagsService = {
    getTagsByPrefix: async function (prefix: string, limit: number): Promise<{ tags: Tag[]; }> {
        try{
            const request = await fetch(`${DATA_PROVIDER_URL}/post-tag?tagPrefix=${prefix}&limit=${limit}`)
            if(!request.ok){
                throw new Error("Coś poszło nie tak")
            }

            const data = await request.json()

            return data; 
        }
        catch(a: any){
            throw new Error("Coś poszło nie tak")
        }
    }
}