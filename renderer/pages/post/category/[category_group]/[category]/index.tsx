import { NextPage } from 'next'
import { useRouter } from 'next/router';
import React from 'react'


const PostCategoryPage: NextPage = () => {
    const router = useRouter();
    const { category_group, category } = router.query;
  
    return <div>PostCategoryPage {`${category_group} -> ${category}`}</div>;
}

export default PostCategoryPage;